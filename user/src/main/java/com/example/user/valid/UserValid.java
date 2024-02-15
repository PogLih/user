package com.example.user.valid;

import com.example.user.request.BaseRequest;
import com.example.user.response.BaseResponse;
import org.springframework.stereotype.Component;

@Component
public class UserValid implements BaseValid{
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
}
