package com.example.user.common.utils;


import com.example.user.application.config.properties.JwtTokenProperties;
import com.example.user.application.config.properties.RefreshTokenProperties;
import com.example.user.common.entity.User;
import com.example.user.common.model.AuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Date;
import java.time.OffsetDateTime;
import java.util.Base64;
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

    private final Pattern BEARER_PATTERN = Pattern.compile("Bearer\\s(?<tokenValue>.+)");
    private static final Integer ACCESS_TOKEN_LENGTH = 64;
    private final JwtTokenProperties jwtTokenProperties;
    private final RefreshTokenProperties refreshTokenProperties;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private static final String SECRET_KEY = "MoNiToRiNg";

//    private final TokenRepository tokenRepository;

    public String getHeaderValueFromRequest(HttpServletRequest request, String header) {
        return request.getHeader(header);
    }

    public String getJwtTokenValueFromRequest(HttpServletRequest request) {
        try {
            String headerVal = this.getHeaderValueFromRequest(request, HttpHeaders.AUTHORIZATION);
            if (Objects.isNull(headerVal)) {
                return null;
            }

            Matcher matcher = BEARER_PATTERN.matcher(headerVal);
            if (matcher.matches()) {
                return matcher.group("tokenValue");
            }
            return null;
        } catch (Exception ex) {
            log.error("Get header value failed with reason: {}", ex.getMessage());
            return null;
        }
    }

    public AuthenticationToken generateJWT(User user) {
//        Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) publicKey, (RSAPrivateKey) privateKey);
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        OffsetDateTime issueAt = OffsetDateTime.now();
        OffsetDateTime jwtExpiredAt = issueAt.plusSeconds(jwtTokenProperties.getTtlInSecond());
        OffsetDateTime refreshTokenExpiredAt = issueAt.plusSeconds(
                refreshTokenProperties.getTtlInSecond()
        );
        Tokens tokens = new Tokens().setToken(RandomStringUtils.randomAlphanumeric(ACCESS_TOKEN_LENGTH)).setUser(user).setExpiredAt(refreshTokenExpiredAt);
        tokenRepository.save(tokens);

        Jwts.builder()
                .id(tokens)
                .claims(Map.of("userId",user.getId().toString()))
                .issuer("userApplication")
                .subject(user.getUsername())
                .expiration(new Date(System.currentTimeMillis() + 3600 * 1000))
                .notBefore(new Date(System.currentTimeMillis()))
                .issuedAt(new Date(System.currentTimeMillis()))
                .signWith(new PrivateKey() {
                    @Override
                    public String getAlgorithm() {
                        return null;
                    }

                    @Override
                    public String getFormat() {
                        return null;
                    }

                    @Override
                    public byte[] getEncoded() {
                        return new byte[0];
                    }
                });


//        String jwt = Jwt.create()
//                .withIssuer(jwtTokenProperties.getIssuer())
//                .withSubject(tokens.getToken())
//                .withClaim("userId", user.getId().toString())
//                .withIssuedAt(DateUtils.convertLocalDateTimeToDate(issueAt.toLocalDateTime()))
//                .withExpiresAt(DateUtils.convertLocalDateTimeToDate(jwtExpiredAt.toLocalDateTime()))
//                .sign(algorithm);
//
//        String refreshToken = JWT.create()
//                .withIssuer(refreshTokenProperties.getIssuer())
//                .withSubject(tokens.getToken())
//                .withIssuedAt(DateUtils.convertLocalDateTimeToDate(issueAt.toLocalDateTime()))
//                .withExpiresAt(DateUtils.convertLocalDateTimeToDate(refreshTokenExpiredAt.toLocalDateTime()))
//                .sign(algorithm);

        return new AuthenticationToken().setJwt(jwt).setRefreshToken(refreshToken);
    }

//    public DecodedJWT decodedJWTToken(String token) {
//        return this.decodedToken(token, jwtTokenProperties.getIssuer());
//    }
//
//    public DecodedJWT decodedRefreshToken(String token) {
//        return this.decodedToken(token, refreshTokenProperties.getIssuer());
//    }
//
//    private DecodedJWT decodedToken(String token, String issuer) {
//        try {
////            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) publicKey, (RSAPrivateKey) privateKey);
//            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
//            JWTVerifier verifier = JWT
//                    .require(algorithm)
//                    .withIssuer(issuer)
//                    .build();
//            return verifier.verify(token);
//        } catch (JWTVerificationException exception) {
//            log.error("Invalid token with reason: {}", exception.getMessage());
//        } catch (Exception ex) {
//            log.error("Can not verify jwt with reason: {}", ex.getMessage());
//        }
//        return null;
//    }

}
class AsymmetricKey {

    public static KeyPair generateRSAKeyPair(String publicKey,String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return new KeyPair(getPublicKey(StringUtils.hasLength(publicKey)?publicKey:JWT_DEFAULT_PUBLIC_KEY)
                ,getPrivateKey(StringUtils.hasLength(privateKey)?privateKey:JWT_DEFAULT_PRIVATE_KEY));
    }
    public static PublicKey getPublicKey(String publicKeyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    public static PrivateKey getPrivateKey(String privateKeyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }
}
class SymmetricKey {
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 256;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    public static SecretKey generateSecretKey(String passphrase, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(passphrase.toCharArray(), salt.getBytes(), ITERATION_COUNT, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        SecretKey secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
        return secretKey;
    }
}