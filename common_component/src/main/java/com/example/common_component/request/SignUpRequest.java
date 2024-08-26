package com.example.common_component.request;

import java.util.Objects;

public class SignUpRequest extends BaseRequest {

  private final String username;
  private final String password;
  private final String email;

  public SignUpRequest(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
  }


  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getEmail() {
    return email;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    var that = (SignUpRequest) obj;
    return Objects.equals(this.username, that.username) &&
        Objects.equals(this.password, that.password) &&
        Objects.equals(this.email, that.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, password, email);
  }

  @Override
  public String toString() {
    return "SignUpRequest[" +
        "username=" + username + ", " +
        "password=" + password + ", " +
        "email=" + email + ']';
  }

}
