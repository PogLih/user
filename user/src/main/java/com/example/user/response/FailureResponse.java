package com.example.user.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FailureResponse<D> extends BaseResponse {

  private String message;
  private String errorCode;
  private D errors;

  public FailureResponse() {
    super.setSuccess(false);
    super.setStatus(HttpStatus.BAD_REQUEST);
  }

  @Override
  public ResponseEntity<FailureResponse<D>> build() {
    return ResponseEntity.status(super.getStatus()).body(this);
  }

}
