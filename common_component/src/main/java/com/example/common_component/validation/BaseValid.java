package com.example.common_component.validation;

import com.example.common_component.dto.request.BaseRequest;

public interface BaseValid<T> {

  boolean validInsert(BaseRequest request);

  boolean validUpdate(BaseRequest request);

  boolean validDelete(BaseRequest request);

  boolean validGet(BaseRequest request);

  boolean validCheck(BaseRequest request);

  boolean validDisable(BaseRequest request);

  default boolean insertTempValidation(BaseRequest baseRequest) {
    return true;//SuccessResponse.builder().build();
  }

  default boolean updateTempValidation(BaseRequest baseRequest) {
    return true;//SuccessResponse.builder().build();
  }

  default boolean selectValidation(BaseRequest baseRequest) {
    return true;//SuccessResponse.builder().build();
  }

  default boolean selectListValidation(BaseRequest baseRequest) {
    return true;//SuccessResponse.builder().build();
  }

  default boolean otherValidation(BaseRequest baseRequest) {
    return true;//SuccessResponse.builder().build();
  }

  default <T> boolean entityInsertValidation(T data) {
    return true;//SuccessResponse.builder().build();
  }

  default <T> boolean entityUpdateValidation(T data) {
    return true;//SuccessResponse.builder().build();
  }

  default <T> boolean entityDisableValidation(T data) {
    return true;//SuccessResponse.builder().build();
  }

  default <T> boolean entityDeleteValidation(T data) {
    return true;//SuccessResponse.builder().build();
  }
}
