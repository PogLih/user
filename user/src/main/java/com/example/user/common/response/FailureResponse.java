package com.example.user.common.response;

import com.example.user.common.enums.ErrorCodeEnum;
import org.springframework.http.HttpStatus;

import java.util.Iterator;

public class FailureResponse extends BaseResponse {

    public FailureResponse() {
        super(HttpStatus.BAD_REQUEST);
        super.setSuccess(false);
    }

    public FailureResponse setErrorMessage(Throwable throwable) {
        super.addPayloadKey("errorMessage", throwable.getMessage());
        return this;
    }

    public FailureResponse setErrorMessage(String message) {
        super.addPayloadKey("message", message);
        return this;
    }

    public FailureResponse setErrorCode(ErrorCodeEnum errorCodeEnum) {
        super.addPayloadKey("errorCode", errorCodeEnum);
        return this;
    }

    public FailureResponse setErrors(Iterator<Object> objects) {
        super.addPayloadKey("errors", objects);
        return this;
    }

}