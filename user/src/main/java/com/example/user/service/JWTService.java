package com.example.user.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.function.Function;

public interface JWTService {
    String extractUsername(String token);
    boolean isTokenValid(String token, UserDetails userDetails);

}
