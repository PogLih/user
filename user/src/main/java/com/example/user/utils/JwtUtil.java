package com.example.user.utils;


import static com.example.common_component.constant.UserConstant.JWT_DEFAULT_PRIVATE_KEY;
import static com.example.common_component.constant.UserConstant.JWT_DEFAULT_PUBLIC_KEY;

import com.example.common_component.enums.TokenTypeEnum;
import com.example.common_component.model.AuthenticationToken;
import com.example.common_component.utils.DateUtil;
import com.example.data_component.entity.Token;
import com.example.data_component.entity.User;
import com.example.data_component.repository.TokenRepository;
import com.example.user.application.config.properties.JwtTokenProperties;
import com.example.user.application.config.properties.RefreshTokenProperties;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
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
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtUtil {

  private static final Integer ACCESS_TOKEN_LENGTH = 64;
  private final Pattern BEARER_PATTERN = Pattern.compile("Bearer\\s(?<tokenValue>.+)");
  private final JwtTokenProperties jwtTokenProperties;
  private final RefreshTokenProperties refreshTokenProperties;
  private final TokenRepository tokenRepository;

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

  public AuthenticationToken generateJWT(User user) throws Exception {
    Date issueAt = new Date(System.currentTimeMillis());
    Date jwtExpiredAt = DateUtil.plusOrMinus(issueAt, jwtTokenProperties.getTtlInSecond(),
        ChronoUnit.SECONDS);
    Date refreshTokenExpiredAt = DateUtil.plusOrMinus(issueAt,
        refreshTokenProperties.getTtlInSecond(), ChronoUnit.SECONDS);

    Token jwtToken = Token.builder()
        .token(RandomStringUtils.randomAlphanumeric(ACCESS_TOKEN_LENGTH))
        .user(user)
        .expiredAt(OffsetDateTime.now().plusSeconds(jwtTokenProperties.getTtlInSecond()))
        .tokenType(TokenTypeEnum.Authentication)
        .build();
    tokenRepository.save(jwtToken);

    Token refreshToken = Token.builder()
        .token(RandomStringUtils.randomAlphanumeric(ACCESS_TOKEN_LENGTH))
        .user(user)
        .expiredAt(OffsetDateTime.now().plusSeconds(refreshTokenProperties.getTtlInSecond()))
        .tokenType(TokenTypeEnum.Refresh)
        .build();
    tokenRepository.save(refreshToken);

    JwtBuilder jwtBuilder = Jwts.builder()
        .claims(Map.of("userId", user.getId().toString()))
        .issuer(jwtTokenProperties.getIssuer())
        .subject(jwtToken.getToken())
        .expiration(jwtExpiredAt)
        .notBefore(issueAt)
        .issuedAt(issueAt);
    String jwt = signJwt(jwtBuilder, user, jwtToken.getToken()).compact();

    String refresh = Jwts.builder()
        .issuer(refreshTokenProperties.getIssuer())
        .issuedAt(issueAt)
        .subject(refreshToken.getToken())
        .expiration(refreshTokenExpiredAt)
        .compact();

    return AuthenticationToken.builder()
        .jwt(jwt)
        .refreshToken(refresh)
        .build();
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

  public static void generateSecretKey(String passphrase, String keyPath) throws Exception {
    SecretKey secretKey = SymmetricKey.generateSecretKey(passphrase, new byte[16]);
    byte[] encoded = secretKey.getEncoded();
    Files.write(new File(keyPath).toPath(), encoded);
  }

  public static void generateRSAKeyPair(String publicKeyPath, String privateKeyPath)
      throws Exception {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(2048);
    KeyPair keyPair = keyPairGenerator.generateKeyPair();

    PublicKey publicKey = keyPair.getPublic();
    PrivateKey privateKey = keyPair.getPrivate();

    Files.write(new File(publicKeyPath).toPath(), publicKey.getEncoded());
    Files.write(new File(privateKeyPath).toPath(), privateKey.getEncoded());
  }
}

class AsymmetricKey {

  private static final String ALGORITHM = "RSA";

  public static KeyPair generateRSAKeyPair(String publicKey, String privateKey)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    return new KeyPair(getPublicKey(StringUtils.hasLength(publicKey) ? publicKey :
        JWT_DEFAULT_PUBLIC_KEY),
        getPrivateKey(StringUtils.hasLength(privateKey) ? privateKey :
            JWT_DEFAULT_PRIVATE_KEY));
  }

  public static PublicKey getPublicKey(String publicKeyString) throws NoSuchAlgorithmException,
      InvalidKeySpecException {
    byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
    return keyFactory.generatePublic(keySpec);
  }

  public static PrivateKey getPrivateKey(byte[] privateKeyByte) throws NoSuchAlgorithmException
      , InvalidKeySpecException {
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

  public static PrivateKey getPrivateKey(String privateKeyString)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
    return keyFactory.generatePrivate(keySpec);
  }
}

class SymmetricKey {

  private static final int ITERATION_COUNT = 65536;
  private static final int KEY_LENGTH = 256;
  private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

  public static SecretKey generateSecretKey(String passphrase, byte[] salt)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    PBEKeySpec spec = new PBEKeySpec(passphrase.toCharArray(), salt, ITERATION_COUNT,
        KEY_LENGTH);
    SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
    byte[] keyBytes = factory.generateSecret(spec).getEncoded();

    return new SecretKeySpec(keyBytes, "HmacSHA256");
  }
}