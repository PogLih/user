package com.example.user.valid;

import com.example.user.request.BaseRequest;
import com.example.user.response.BaseResponse;

public interface BaseValid{
    BaseResponse validInsert (BaseRequest request);
    BaseResponse validUpdate (BaseRequest request);
    BaseResponse validDelete (BaseRequest request);
    BaseResponse validGet (BaseRequest request);
    BaseResponse validCheck (BaseRequest request);


}
