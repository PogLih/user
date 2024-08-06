package com.example.user.controller;


import com.example.common_component.request.SignUpRequest;
import com.example.common_component.response.ResponseData;
import com.example.user.service.impl.AuthServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthAPI {

  private final AuthServiceImpl authService;

  //    @PostMapping(value = {"/login"},consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//    public ResponseEntity<?> handleLoginRequest(@ModelAttribute @Valid LoginRequest
//    loginRequest) throws Exception {
//        return authService.login(loginRequest).build();
//    }
  @PostMapping(value = {"/sign-up"})
  public ResponseData handleSignUpRequest(@RequestBody @Valid SignUpRequest loginRequest)
      throws Exception {
    return authService.signup(loginRequest);
  }
//    @PostMapping(value = {"/refresh"})
//    public ResponseEntity<?> handleRefreshRequest(@ModelAttribute @Valid LoginRequest
//    loginRequest) throws Exception {
//        return authService.login(loginRequest).build();
//    }
}
