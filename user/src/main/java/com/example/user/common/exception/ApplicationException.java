package com.example.user.common.exception;

import lombok.Getter;

public class ApplicationException extends RuntimeException {

    @Getter
    private String errorCode;

    public ApplicationException() {
        super();
    }

    public ApplicationException(String code) {
        super(code);
        this.errorCode = code;
    }

    public ApplicationException(String code, Throwable cause) {
        super(code, cause);
        this.errorCode = code;
    }

}
