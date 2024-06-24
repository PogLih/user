package com.example.user.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@Accessors(chain = true)
public abstract class BaseResponse {

  private Boolean success;

  @JsonIgnore
  private HttpStatus status;

  public abstract ResponseEntity<?> build();

}
