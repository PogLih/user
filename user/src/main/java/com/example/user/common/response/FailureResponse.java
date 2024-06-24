package com.example.user.common.response;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.http.ResponseEntity;
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Setter
@Builder
public class FailureResponse<D> extends BaseResponse {

  private String message;
  private String errorCode;
  private D errors;
  @Override
  public ResponseEntity<FailureResponse<D>> build() {
    return ResponseEntity.status(super.getStatus()).body(this);
  }

}
