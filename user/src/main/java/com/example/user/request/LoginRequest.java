package com.example.user.request;

import com.example.user.annotation.RequestType;
import com.example.user.annotation.RequestTypeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
@Getter
@Setter
@Builder
@RequestType(value = RequestTypeEnum.CHECK)
public class LoginRequest extends BaseRequest{
    private final String username;
    private final String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
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
