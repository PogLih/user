package com.example.user.application.controller;


import com.example.user.application.service.impl.AuthServiceImpl;
import com.example.user.common.request.SignUpRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> handleSignUpRequest(@RequestBody @Valid SignUpRequest loginRequest) throws Exception {
        return authService.signup(loginRequest).build();
    }
//    @PostMapping(value = {"/refresh"})
//    public ResponseEntity<?> handleRefreshRequest(@ModelAttribute @Valid LoginRequest
//    loginRequest) throws Exception {
//        return authService.login(loginRequest).build();
//    }
}
