package com.example.user.request;

import lombok.Data;

import java.util.Objects;
@Data
public class SignUpRequest extends BaseRequest{
    private final String username;
    private final String password;
    private final String email;

    public SignUpRequest(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }

    public String email() {
        return email;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
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
