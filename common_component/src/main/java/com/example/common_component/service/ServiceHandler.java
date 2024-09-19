package com.example.common_component.service;

import com.example.common_component.dto.request.BaseRequest;

public class ServiceHandler {

  public ServiceHandler() {

  }

  public interface WriteHandler<T> {

  }

  public interface WriteEntityHandler<T> extends WriteHandler<T> {

    T onChangeWriteEntityHandled(BaseRequest baseRequest);
  }
}