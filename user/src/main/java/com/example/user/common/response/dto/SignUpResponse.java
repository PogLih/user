package com.example.user.common.response.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Builder
public class SignUpResponse {
    private String jwt;
    private String refreshToken;
}
