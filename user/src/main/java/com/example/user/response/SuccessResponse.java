package com.example.user.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SuccessResponse<D> extends BaseResponse {
  private D data;

  public SuccessResponse() {
    super.setSuccess(true);
    super.setStatus(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<SuccessResponse<D>> build() {
    return ResponseEntity.status(super.getStatus()).body(this);
  }
}
