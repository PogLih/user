package com.example.common_component.response;

import com.example.common_component.dto.BaseResponse;

public class ResponseData extends BaseResponse {

  private Object data;

  public ResponseData(Object data) {
    this.data = data;
  }

  public ResponseData() {
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }
}
