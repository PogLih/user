package com.example.user.response.common;

import com.example.user.response.BaseResponse;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Builder
public class SignUpResponse extends BaseResponse {
    @Override
    public ResponseEntity<?> build() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
