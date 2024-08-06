package com.example.common_component.request;

import com.example.common_component.anotations.RequestValidation;
import com.example.common_component.enums.RequestTypeEnum;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@RequestValidation(value = RequestTypeEnum.Other)
public class LoginRequest extends BaseRequest {

  private final String username;
  private final String password;

  public LoginRequest(String username, String password) {
    this.username = username;
    this.password = password;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    var that = (LoginRequest) obj;
    return Objects.equals(this.username, that.username) &&
        Objects.equals(this.password, that.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, password);
  }

  @Override
  public String toString() {
    return "LoginRequest[" +
        "username=" + username + ", " +
        "password=" + password + ']';
  }

}
