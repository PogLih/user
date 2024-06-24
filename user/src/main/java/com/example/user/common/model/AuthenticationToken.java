package com.example.user.common.model;

import lombok.Data;

@Data
public class AuthenticationToken {
    private String jwt;
    private String refreshToken;
}
