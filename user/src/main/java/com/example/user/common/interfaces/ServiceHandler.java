package com.example.user.common.interfaces;

import com.example.user.common.request.BaseRequest;

public class ServiceHandler{
    public ServiceHandler(){

    }
    public interface WriteHandler<T> {
    }

    public interface WriteEntityHandler<T> extends WriteHandler<T> {
        T onChangeWriteEntityHandled(BaseRequest baseRequest);
    }
}