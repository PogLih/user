package com.example.user.controller;


import com.example.common_component.dto.ApiResponse;
import com.example.common_component.dto.request.AuthenticationRequest;
import com.example.common_component.dto.request.IntrospectRequest;
import com.example.common_component.dto.request.LogoutRequest;
import com.example.common_component.dto.request.RefreshRequest;
import com.example.common_component.dto.response.AuthenticationResponse;
import com.example.common_component.dto.response.IntrospectResponse;
import com.example.user.service.AuthService;
import com.nimbusds.jose.JOSEException;
import jakarta.annotation.PostConstruct;
import java.text.ParseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  private final AuthService authService;

  @PostConstruct
  public void init() {
    log.info("AuthAPI initialized");
  }


  @PostMapping(value = "/sign-in")
  public ApiResponse<AuthenticationResponse> handleSignInRequest(
      @RequestBody AuthenticationRequest request) throws Exception {
    log.info("Received sign-in request");
    return ApiResponse.<AuthenticationResponse>builder().result(authService.signIn(request))
        .build();
  }

  @PostMapping(value = "/verify-token")
  public ApiResponse<IntrospectResponse> handleVerifyTokenRequest(
      @RequestBody IntrospectRequest request) throws ParseException, JOSEException {
    log.info("Received verify-token request");
    return ApiResponse.<IntrospectResponse>builder().result(authService.verify(request)).build();
  }

  @PostMapping("/refresh")
  public ApiResponse<AuthenticationResponse> authenticate(@RequestBody RefreshRequest request)
      throws ParseException, JOSEException {
    var result = authService.refreshToken(request);
    return ApiResponse.<AuthenticationResponse>builder().result(result).build();
  }

  @PostMapping("/logout")
  public ApiResponse<Void> logout(@RequestBody LogoutRequest request)
      throws ParseException, JOSEException {
    authService.logOut(request);
    return ApiResponse.<Void>builder().build();
  }
}
