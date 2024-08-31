package com.example.user.service;

import com.example.common_component.dto.response.UserResponse;
import com.example.common_component.request.SignUpRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface AuthService {

  Authentication authenticate(HttpServletRequest request);

  UserResponse signup(SignUpRequest signUpRequest) throws Exception;
}
