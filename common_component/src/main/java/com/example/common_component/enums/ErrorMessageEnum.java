package com.example.common_component.enums;

import lombok.Getter;

@Getter
public enum ErrorMessageEnum {

  DEFAULT_MESSAGE("Can not perform this request. Please contact admin");

  private final String message;

  ErrorMessageEnum(String message) {
    this.message = message;
  }
}
