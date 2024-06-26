package com.example.user.common.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationToken {
    private String jwt;
    private String refreshToken;
}
