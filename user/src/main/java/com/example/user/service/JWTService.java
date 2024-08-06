package com.example.user.service;

import com.example.data_component.entity.User;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {

  String extractUsername(String token);

  String generateToken(UserDetails userDetails);

  boolean isTokenValid(String token, UserDetails userDetails);

  String generateRefreshToken(Map<String, Object> extraClaims, User user);
}
