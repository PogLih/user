package com.example.user.common.interfaces;

import com.example.user.common.request.BaseRequest;
import com.example.user.common.response.BaseResponse;

public interface BaseValid<T> {
    BaseResponse validInsert(BaseRequest request);

    BaseResponse validUpdate(BaseRequest request);

    BaseResponse validDelete(BaseRequest request);

    BaseResponse validGet(BaseRequest request);

    BaseResponse validCheck(BaseRequest request);

    BaseResponse validDisable(BaseRequest request);

    default BaseResponse insertTempValidation(BaseRequest baseRequest) {
        return null;//SuccessResponse.builder().build();
    }

    default BaseResponse updateTempValidation(BaseRequest baseRequest) {
        return null;//SuccessResponse.builder().build();
    }

    default BaseResponse selectValidation(BaseRequest baseRequest) {
        return null;//SuccessResponse.builder().build();
    }

    default BaseResponse selectListValidation(BaseRequest baseRequest) {
        return null;//SuccessResponse.builder().build();
    }

    default BaseResponse otherValidation(BaseRequest baseRequest) {
        return null;//SuccessResponse.builder().build();
    }

    default <T> BaseResponse entityInsertValidation(T data) {
        return null;//SuccessResponse.builder().build();
    }

    default <T> BaseResponse entityUpdateValidation(T data) {
        return null;//SuccessResponse.builder().build();
    }

    default <T> BaseResponse entityDisableValidation(T data) {
        return null;//SuccessResponse.builder().build();
    }

    default <T> BaseResponse entityDeleteValidation(T data) {
        return null;//SuccessResponse.builder().build();
    }
}
