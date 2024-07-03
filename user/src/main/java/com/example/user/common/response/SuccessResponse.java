package com.example.user.common.response;

import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;


@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SuccessResponse extends BaseResponse {

    public SuccessResponse() {
        super(HttpStatus.OK);
        super.setSuccess(true);
    }

    public SuccessResponse(Object data) {
        this();
        super.addPayloadKey("data", data);
    }


}
