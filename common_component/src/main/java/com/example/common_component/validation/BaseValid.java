package com.example.common_component.validation;

import com.example.common_component.request.BaseRequest;
import com.example.common_component.response.ResponseData;

public interface BaseValid<T> {

  ResponseData validInsert(BaseRequest request);

  ResponseData validUpdate(BaseRequest request);

  ResponseData validDelete(BaseRequest request);

  ResponseData validGet(BaseRequest request);

  ResponseData validCheck(BaseRequest request);

  ResponseData validDisable(BaseRequest request);

  default ResponseData insertTempValidation(BaseRequest baseRequest) {
    return null;//SuccessResponse.builder().build();
  }

  default ResponseData updateTempValidation(BaseRequest baseRequest) {
    return null;//SuccessResponse.builder().build();
  }

  default ResponseData selectValidation(BaseRequest baseRequest) {
    return null;//SuccessResponse.builder().build();
  }

  default ResponseData selectListValidation(BaseRequest baseRequest) {
    return null;//SuccessResponse.builder().build();
  }

  default ResponseData otherValidation(BaseRequest baseRequest) {
    return null;//SuccessResponse.builder().build();
  }

  default <T> ResponseData entityInsertValidation(T data) {
    return null;//SuccessResponse.builder().build();
  }

  default <T> ResponseData entityUpdateValidation(T data) {
    return null;//SuccessResponse.builder().build();
  }

  default <T> ResponseData entityDisableValidation(T data) {
    return null;//SuccessResponse.builder().build();
  }

  default <T> ResponseData entityDeleteValidation(T data) {
    return null;//SuccessResponse.builder().build();
  }
}
