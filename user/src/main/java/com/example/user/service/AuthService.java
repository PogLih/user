package com.example.user.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface AuthService {
    Authentication authenticate(HttpServletRequest request);
}
