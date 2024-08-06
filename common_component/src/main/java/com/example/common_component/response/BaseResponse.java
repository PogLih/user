package com.example.common_component.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class BaseResponse {

  @Override
  public String toString() {
    try {
      return String.format("<%s@%s> JSON: %s", this.getClass().getName(),
          System.identityHashCode(this),
          new ObjectMapper().writeValueAsString(this));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return "";
  }
}
