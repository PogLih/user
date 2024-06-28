package com.example.user.common.utils;


import com.example.user.application.config.properties.JwtTokenProperties;
import com.example.user.application.config.properties.RefreshTokenProperties;
import com.example.user.common.entity.Token;
import com.example.user.common.entity.User;
import com.example.user.common.enums.TokenTypeEnum;
import com.example.user.common.model.AuthenticationToken;
import com.example.user.database.repository.TokenRepository;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.user.common.constant.UserConstant.JWT_DEFAULT_PRIVATE_KEY;
import static com.example.user.common.constant.UserConstant.JWT_DEFAULT_PUBLIC_KEY;


@Log4j2
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private static final Integer ACCESS_TOKEN_LENGTH = 64;
    private final Pattern BEARER_PATTERN = Pattern.compile("Bearer\\s" +
            "(?<tokenValue>.+)");
    private final JwtTokenProperties jwtTokenProperties;
    private final RefreshTokenProperties refreshTokenProperties;

    private final TokenRepository tokenRepository;

    public String getHeaderValueFromRequest(HttpServletRequest request,
                                            String header) {
        return request.getHeader(header);
    }

    public String getJwtTokenValueFromRequest(HttpServletRequest request) {
        try {
            String headerVal = this.getHeaderValueFromRequest(request,
                    HttpHeaders.AUTHORIZATION);
            if (Objects.isNull(headerVal)) {
                return null;
            }

            Matcher matcher = BEARER_PATTERN.matcher(headerVal);
            if (matcher.matches()) {
                return matcher.group("tokenValue");
            }
            return null;
        } catch (Exception ex) {
            log.error("Get header value failed with reason: {}",
                    ex.getMessage());
            return null;
        }
    }

    public AuthenticationToken generateJWT(User user) throws Exception {
        Date issueAt = new Date(System.currentTimeMillis());
        Date jwtExpiredAt = DateUtil.plusOrMinus(issueAt, jwtTokenProperties.getTtlInSecond(),
                ChronoUnit.SECONDS);
        Date refreshTokenExpiredAt = DateUtil.plusOrMinus(issueAt,
                refreshTokenProperties.getTtlInSecond(),
                ChronoUnit.SECONDS);

        Token jwtToken = Token.builder().token(RandomStringUtils
                        .randomAlphanumeric(ACCESS_TOKEN_LENGTH))
                .user(user).expiredAt(OffsetDateTime.now().plusSeconds(jwtTokenProperties.getTtlInSecond()))
                .tokenType(TokenTypeEnum.Authentication).build();
        tokenRepository.save(jwtToken);
        Token refreshToken =
                Token.builder().token(RandomStringUtils.randomAlphanumeric(ACCESS_TOKEN_LENGTH))
                        .user(user).expiredAt(OffsetDateTime.now().plusSeconds(refreshTokenProperties.getTtlInSecond()))
                        .tokenType(TokenTypeEnum.Refresh).build();
        tokenRepository.save(refreshToken);

        JwtBuilder jwtBuilder = Jwts.builder().claims(Map.of("userId",
                        user.getId().toString())).issuer(jwtTokenProperties.getIssuer()).subject(jwtToken.getToken())
                .expiration(jwtExpiredAt).notBefore(issueAt).issuedAt(issueAt);
        String jwt = signJwt(jwtBuilder, user, jwtToken.getToken()).compact();

        String refresh =
                Jwts.builder().issuer(refreshTokenProperties.getIssuer()).issuedAt(issueAt)
                        .subject(refreshToken.getToken()).expiration(refreshTokenExpiredAt).compact();

        return AuthenticationToken.builder().jwt(jwt).refreshToken(refresh).build();
    }

    private JwtBuilder signJwt(JwtBuilder jwtBuilder, User user, String token) throws Exception {
        if (jwtTokenProperties.getType().equals("Asymmetric")) {
            return jwtBuilder.signWith(getPrivateKey());
        } else if (jwtTokenProperties.getType().equals("Symmetric")) {
            return jwtBuilder.signWith(getSecretKey(user.getPassword(), token));
        }
        return jwtBuilder;
    }


    private SecretKey getSecretKey(String pass, String token) throws Exception {
        byte[] keyBytes =
                Files.readAllBytes(new File(jwtTokenProperties.getSecretKeyPath()).toPath());
        return SymmetricKey.generateSecretKey(pass, keyBytes);
    }

    private PrivateKey getPrivateKey() throws Exception {
        byte[] keyBytes =
                Files.readAllBytes(new File(jwtTokenProperties.getPrivateKeyPath()).toPath());
        return AsymmetricKey.getPrivateKey(keyBytes);
    }

    private PublicKey getPublicKey() throws Exception {
        byte[] keyBytes =
                Files.readAllBytes(new File(jwtTokenProperties.getPublicKeyPath()).toPath());
        return AsymmetricKey.getPublicKey(keyBytes);
    }

}

class AsymmetricKey {
    private static final String ALGORITHM = "SHA256withRSA";

    public static KeyPair generateRSAKeyPair(String publicKey,
                                             String privateKey) throws NoSuchAlgorithmException,
            InvalidKeySpecException {
        return new KeyPair(getPublicKey(StringUtils.hasLength(publicKey) ?
                publicKey : JWT_DEFAULT_PUBLIC_KEY),
                getPrivateKey(StringUtils.hasLength(privateKey) ? privateKey
                        : JWT_DEFAULT_PRIVATE_KEY));
    }

    public static PublicKey getPublicKey(String publicKeyString) throws NoSuchAlgorithmException,
            InvalidKeySpecException {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }

    public static PrivateKey getPrivateKey(byte[] privateKeyByte) throws NoSuchAlgorithmException,
            InvalidKeySpecException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyByte);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }

    public static PublicKey getPublicKey(byte[] publicKeyByte) throws NoSuchAlgorithmException,
            InvalidKeySpecException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyByte);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }

    public static PrivateKey getPrivateKey(String privateKeyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }
}

class SymmetricKey {
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 256;
    private static final String ALGORITHM = "HmacSHA256";

    //    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    public static SecretKey generateSecretKey(String passphrase, byte[] salt) throws NoSuchAlgorithmException,
            InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(passphrase.toCharArray(),
                salt, ITERATION_COUNT, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();

        SecretKey secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
        return secretKey;
    }
}