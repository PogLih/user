package com.example.user.service.impl;

import com.example.common_component.dto.request.AuthenticationRequest;
import com.example.common_component.dto.request.IntrospectRequest;
import com.example.common_component.dto.request.LogoutRequest;
import com.example.common_component.dto.request.RefreshRequest;
import com.example.common_component.dto.response.AuthenticationResponse;
import com.example.common_component.dto.response.IntrospectResponse;
import com.example.data_component.entity.User;
import com.example.data_component.repository.RoleRepository;
import com.example.data_component.repository.UserRepository;
import com.example.data_component.specification.RoleSpecification;
import com.example.data_component.specification.UserSpecification;
import com.example.user.exception.ApplicationException;
import com.example.user.exception.ErrorCode;
import com.example.user.service.AuthService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {


  private final UserRepository userRepository;
  private final UserSpecification userSpecification;
  private final RoleRepository roleRepository;
  private final RoleSpecification roleSpecification;
  private final PasswordEncoder passwordEncoder;
  private final ModelMapper modelMapper;
  @NonFinal
  @Value("${jwt.signKey}")
  private String SIGN_KEY;


  @Override
  public AuthenticationResponse signIn(AuthenticationRequest request) throws Exception {
    User user = userRepository.findOne(userSpecification.getByName(request.getUsername()))
        .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_EXISTED));
    boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
    if (!authenticated) {
      throw new ApplicationException(ErrorCode.UNAUTHENTICATED);
    }
    String token = generateToken(user);
    return AuthenticationResponse.builder().authenticated(authenticated).token(token).build();
  }

  @Override
  public IntrospectResponse verify(IntrospectRequest request) throws JOSEException, ParseException {
    String token = request.getToken();
    JWSVerifier jwsVerifier = new MACVerifier(SIGN_KEY.getBytes());
    SignedJWT signedJWT = SignedJWT.parse(token);
    Date expiredTime = signedJWT.getJWTClaimsSet().getExpirationTime();
    boolean verified = signedJWT.verify(jwsVerifier);
    return IntrospectResponse.builder().valid(verified && expiredTime.after(new Date())).build();
  }

  @Override
  public void logOut(LogoutRequest request) {
  }

  @Override
  public AuthenticationResponse refreshToken(RefreshRequest request) {
    return null;
  }

  private String generateToken(User user) {
    JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

    JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder().subject(user.getUsername())
        .issuer("Users Service").issueTime(new Date())
        .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
        .claim("scope", buildScope(user))//custom claims.
        .build();
    Payload payload = new Payload(jwtClaimsSet.toJSONObject());
    JWSObject jwsObject = new JWSObject(jwsHeader, payload);

    try {
      jwsObject.sign(new MACSigner(SIGN_KEY.getBytes()));
      return jwsObject.serialize();
    } catch (JOSEException exception) {
      log.error("Cant not create Token");
      throw new RuntimeException(exception);
    }

  }

  private String buildScope(User user) {
    StringJoiner stringJoiner = new StringJoiner(" ");

    if (!CollectionUtils.isEmpty(user.getRoles())) {
      user.getRoles().forEach(role -> {
        stringJoiner.add("ROLE_" + role.getName());
        if (!CollectionUtils.isEmpty(role.getPermissions())) {
          role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
        }
      });
    }

    return stringJoiner.toString();
  }
}
