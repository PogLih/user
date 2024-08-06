package com.example.user.service;

import com.example.common_component.request.SignUpRequest;
import com.example.common_component.response.ResponseData;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface AuthService {

  Authentication authenticate(HttpServletRequest request);

  ResponseData signup(SignUpRequest signUpRequest) throws Exception;
}
