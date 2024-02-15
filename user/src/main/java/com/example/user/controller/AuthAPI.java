package com.example.user.controller;

import com.example.user.request.LoginRequest;
import com.example.user.service.impl.AuthServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<?> handleLoginRequest(@ModelAttribute @Valid LoginRequest loginRequest) {
        return authService.login(loginRequest).build();
    }
}
