package com.example.user.application.controller;


import com.example.user.common.request.LoginRequest;
import com.example.user.common.request.SignUpRequest;
import com.example.user.application.service.impl.AuthServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthAPI {
    private final AuthServiceImpl authService;

    @PostMapping(value = {"/login"},consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> handleLoginRequest(@ModelAttribute @Valid LoginRequest loginRequest) throws Exception {
        return authService.login(loginRequest).build();
    }
    @GetMapping(value = {"/sign-up"})
    public ResponseEntity<?> handleSignUpRequest(@RequestBody @Valid SignUpRequest loginRequest) throws Exception {
        return authService.signup(loginRequest).build();
    }
    @PostMapping(value = {"/refresh"})
    public ResponseEntity<?> handleRefreshRequest(@ModelAttribute @Valid LoginRequest loginRequest) throws Exception {
        return authService.login(loginRequest).build();
    }
}
