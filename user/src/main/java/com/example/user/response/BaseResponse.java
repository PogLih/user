package com.example.user.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
