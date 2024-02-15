package com.example.user.response;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Builder
public class LoginResponse {

    private String jwt;
    private String refreshToken;

}
