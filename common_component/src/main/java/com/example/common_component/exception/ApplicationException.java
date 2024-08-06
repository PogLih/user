package com.example.common_component.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

  private String errorCode;

  public ApplicationException() {
    super();
  }

  public ApplicationException(String code) {
    super(code);
    this.errorCode = code;
  }

  public ApplicationException(String code, Throwable cause) {
    super(code, cause);
    this.errorCode = code;
  }

}
