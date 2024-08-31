package com.example.user.application.validation;

import com.example.common_component.request.BaseRequest;
import com.example.common_component.response.ResponseData;
import com.example.common_component.validation.BaseValid;
import com.example.data_component.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValid implements BaseValid<User> {

  @Override
  public ResponseData validInsert(BaseRequest request) {
    return null;
  }

  @Override
  public ResponseData validUpdate(BaseRequest request) {
    return null;
  }

  @Override
  public ResponseData validDelete(BaseRequest request) {
    return null;
  }

  @Override
  public ResponseData validGet(BaseRequest request) {
    return null;
  }

  @Override
  public ResponseData validCheck(BaseRequest request) {
    return null;
  }

  @Override
  public ResponseData validDisable(BaseRequest request) {
    return null;
  }
}
