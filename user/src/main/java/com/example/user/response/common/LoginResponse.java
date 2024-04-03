package com.example.user.response.common;

import com.example.user.response.BaseResponse;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.ResponseEntity;

@Data
@Accessors(chain = true)
@Builder
public class LoginResponse extends BaseResponse {

    private String jwt;
    private String refreshToken;

    @Override
    public ResponseEntity<?> build() {
        return ResponseEntity.status(super.getStatus()).body(this);
    }
}
