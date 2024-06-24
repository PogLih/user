package com.example.user.common.response.dto;

import com.example.user.common.response.BaseResponse;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.http.ResponseEntity;

@EqualsAndHashCode(callSuper = true)
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
