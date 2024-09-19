package com.example.common_component.service;

import com.example.common_component.dto.request.BaseRequest;
import com.example.common_component.validation.BaseValid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceManagerFactory {

  private final ModelMapper modelMapper;

  @Autowired
  public ServiceManagerFactory(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public <T, R> ServiceManager<T, R> createServiceManager(BaseRequest baseRequest,
      BaseValid<T> baseValid, Class<R> responseType,
      ServiceHandler.WriteHandler<T> serviceHandler
  ) {
    return ServiceManager.<T, R>builder()
        .modelMapper(modelMapper)
        .baseRequest(baseRequest)
        .baseValid(baseValid)
        .serviceHandler(serviceHandler)
        .responseType(responseType)
        .build();
  }
}