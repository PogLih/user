package com.example.user.common.response;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
@Builder
public class FailureResponse<D> extends BaseResponse {

    private D errors;

    public FailureResponse() {
        super.setStatus(HttpStatus.BAD_REQUEST);
        super.setSuccess(false);
    }

    public FailureResponse(D errors) {
        this();
        this.errors = errors;
    }

    @Override
    public ResponseEntity<FailureResponse<D>> build() {
        return ResponseEntity.status(super.getStatus()).body(this);
    }

}
