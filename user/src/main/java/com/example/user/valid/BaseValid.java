package com.example.user.valid;

import com.example.user.request.BaseRequest;
import com.example.user.response.BaseResponse;
import com.example.user.response.SuccessResponse;
import org.springframework.http.HttpStatus;

public interface BaseValid<T>{
    BaseResponse validInsert (BaseRequest request);
    BaseResponse validUpdate (BaseRequest request);
    BaseResponse validDelete (BaseRequest request);
    BaseResponse validGet (BaseRequest request);
    BaseResponse validCheck (BaseRequest request);
    BaseResponse validDisable (BaseRequest request);

    default BaseResponse insertTempValidation(BaseRequest baseRequest) {
        return SuccessResponse.builder().build();
    }

    default BaseResponse updateTempValidation(BaseRequest baseRequest) {
        return SuccessResponse.builder().build();
    }

    default BaseResponse selectValidation(BaseRequest baseRequest) {
        return SuccessResponse.builder().build();
    }

    default BaseResponse selectListValidation(BaseRequest baseRequest) {
        return SuccessResponse.builder().build();
    }

    default BaseResponse otherValidation(BaseRequest baseRequest) {
        return SuccessResponse.builder().build();
    }

    default <T> BaseResponse entityInsertValidation(T data) {
        return SuccessResponse.builder().build();
    }

    default <T> BaseResponse entityUpdateValidation(T data) {
        return SuccessResponse.builder().build();
    }

    default <T> BaseResponse entityDisableValidation(T data) {
        return SuccessResponse.builder().build();
    }

    default <T> BaseResponse entityDeleteValidation(T data) {
        return SuccessResponse.builder().build();
    }
}
