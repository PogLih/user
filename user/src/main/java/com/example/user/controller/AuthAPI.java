package com.example.user.controller;


import com.example.common_component.dto.ApiResponse;
import com.example.common_component.dto.response.UserResponse;
import com.example.common_component.request.SignUpRequest;
import com.example.user.service.AuthService;
import jakarta.annotation.PostConstruct;
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
public class AuthAPI {

  private final AuthService authService;

  @PostConstruct
  public void init() {
    log.info("AuthAPI initialized");
  }

  @PostMapping(value = {"/sign-up"})
  public ApiResponse<UserResponse> handleSignUpRequest(@RequestBody SignUpRequest signUpRequest)
      throws Exception {
    log.info("Received sign-up request");
    return ApiResponse.<UserResponse>builder()
        .result(authService.signup(signUpRequest))
        .build();
  }

}
