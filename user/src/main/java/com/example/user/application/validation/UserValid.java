package com.example.user.application.validation;

import com.example.common_component.dto.request.BaseRequest;
import com.example.common_component.validation.BaseValid;
import com.example.data_component.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValid implements BaseValid<User> {

  @Override
  public boolean validInsert(BaseRequest request) {
    return false;
  }

  @Override
  public boolean validUpdate(BaseRequest request) {
    return false;
  }

  @Override
  public boolean validDelete(BaseRequest request) {
    return false;
  }

  @Override
  public boolean validGet(BaseRequest request) {
    return false;
  }

  @Override
  public boolean validCheck(BaseRequest request) {
    return false;
  }

  @Override
  public boolean validDisable(BaseRequest request) {
    return false;
  }
}
