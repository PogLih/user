package com.example.user.common.response;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
public class SuccessResponse<D> extends BaseResponse {
    private D data;

    public SuccessResponse() {
        super.setSuccess(true);
        super.setStatus(HttpStatus.OK);
    }

    public SuccessResponse(D data) {
        this();
        this.data = data;
    }

    @Override
    public ResponseEntity<SuccessResponse<D>> build() {
        return ResponseEntity.status(super.getStatus()).body(this);
    }
}
