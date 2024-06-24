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
public class SuccessResponse<D> extends BaseResponse {
  private D data;
  @Override
  public ResponseEntity<SuccessResponse<D>> build() {
    return ResponseEntity.status(super.getStatus()).body(this);
  }
}
