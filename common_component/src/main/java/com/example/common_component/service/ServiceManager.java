package com.example.common_component.service;

import com.example.common_component.anotations.RequestValidation;
import com.example.common_component.enums.RequestTypeEnum;
import com.example.common_component.exception.ApplicationException;
import com.example.common_component.request.BaseRequest;
import com.example.common_component.response.ResponseData;
import com.example.common_component.validation.BaseValid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceManager<T> {

  private BaseRequest baseRequest;
  private ServiceHandler.WriteHandler<T> serviceHandler;
  private BaseValid<T> baseValid;


  public ServiceManager(BaseRequest request) {
    this.baseRequest = request;
  }


  public ServiceManager(BaseValid<T> valid) {
    this.baseValid = valid;
  }

  public ServiceManager(ServiceHandler.WriteEntityHandler<T> serviceHandler) {
    this.serviceHandler = serviceHandler;
  }

  public ResponseData execute() throws Exception {
    if (baseRequest == null) {
      throw new ApplicationException();
    }
    if (baseValid == null) {
      throw new Exception();
    }
    RequestTypeEnum requestType;
    Class<?> clazz = baseRequest.getClass();
    if (clazz.isAnnotationPresent(RequestValidation.class)) {
      RequestValidation annotation = clazz.getAnnotation(RequestValidation.class);
      requestType = annotation.value();
      boolean result = requestValidation(baseValid, requestType);
    }

//        if(requestType.equals(RequestTypeEnum.GET) || requestType.equals(RequestTypeEnum.CHECK)
//         || requestType.equals(RequestTypeEnum.GET_LIST)){
//            if(requestType.equals(RequestTypeEnum.GET_LIST)){
//                List all = jpaRepository.findAll(specification);
//            }else{
//                Optional one = jpaRepository.findOne(specification);
//            }
//        }else{
//            Object result = serviceHandler.handleService();
//            if (jpaRepository == null) {
//                throw new Exception();
//            }
//            this.jpaRepository.saveAndFlush(result);
//        }
    T result =
        ((ServiceHandler.WriteEntityHandler<T>) serviceHandler).onChangeWriteEntityHandled(
            baseRequest);
    ResponseData responseData = new ResponseData();
    responseData.setData(result);
    return responseData;
  }

  private boolean requestValidation(BaseValid<T> baseValid, RequestTypeEnum type) {
    if (type != null && baseValid != null) {
      ResponseData errorMessage = null;
      if (type == RequestTypeEnum.Insert) {
        errorMessage = baseValid.validInsert(baseRequest);
      } else if (type == RequestTypeEnum.InsertTemp) {
        errorMessage = baseValid.insertTempValidation(baseRequest);
      } else if (type == RequestTypeEnum.Update) {
        errorMessage = baseValid.validUpdate(baseRequest);
      } else if (type == RequestTypeEnum.UpdateTemp) {
        errorMessage = baseValid.updateTempValidation(baseRequest);
      } else if (type == RequestTypeEnum.Disable) {
        errorMessage = baseValid.validDisable(baseRequest);
      } else if (type == RequestTypeEnum.Delete) {
        errorMessage = baseValid.validDelete(baseRequest);
      } else if (type == RequestTypeEnum.Select) {
        errorMessage = baseValid.selectValidation(baseRequest);
      } else if (type == RequestTypeEnum.SelectList) {
        errorMessage = baseValid.selectListValidation(baseRequest);
      } else if (type == RequestTypeEnum.Other) {
        errorMessage = baseValid.otherValidation(baseRequest);
      }

      return true;
    } else {
      return false;
    }
  }
}
