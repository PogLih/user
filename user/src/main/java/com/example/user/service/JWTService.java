package com.example.user.service;

import com.example.user.entity.User;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public interface JWTService {
    String extractUsername(String token);
     String generateToken(UserDetails userDetails);
    boolean isTokenValid(String token, UserDetails userDetails);

    String generateRefreshToken(Map<String, Object> extraClaims, User user);
}
