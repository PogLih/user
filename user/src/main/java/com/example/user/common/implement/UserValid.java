package com.example.user.common.implement;

import com.example.user.common.interfaces.BaseValid;
import com.example.user.common.request.BaseRequest;
import com.example.user.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValid implements BaseValid {
    @Override
    public BaseResponse validInsert(BaseRequest request) {
        return null;
    }

    @Override
    public BaseResponse validUpdate(BaseRequest request) {
        return null;
    }

    @Override
    public BaseResponse validDelete(BaseRequest request) {
        return null;
    }

    @Override
    public BaseResponse validGet(BaseRequest request) {
        return null;
    }

    @Override
    public BaseResponse validCheck(BaseRequest request) {
        return null;
    }

    @Override
    public BaseResponse validDisable(BaseRequest request) {
        return null;
    }
}
