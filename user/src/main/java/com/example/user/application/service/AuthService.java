package com.example.user.application.service;

import com.example.user.common.request.SignUpRequest;
import com.example.user.common.response.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface AuthService {
    Authentication authenticate(HttpServletRequest request);

    BaseResponse signup(SignUpRequest signUpRequest) throws Exception;
}
