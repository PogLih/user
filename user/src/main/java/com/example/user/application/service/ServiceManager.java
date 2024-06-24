package com.example.user.application.service;

import com.example.user.common.annotation.RequestTypeEnum;
import com.example.user.common.annotation.RequestValidation;
import com.example.user.database.repository.BaseRepository;
import com.example.user.common.request.BaseRequest;
import com.example.user.common.response.BaseResponse;
import com.example.user.common.response.SuccessResponse;
import com.example.user.common.interfaces.BaseValid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ServiceManager<T> {
    private BaseRequest baseRequest;
    private BaseRepository jpaRepository;
    private Specification specification;
    private ServiceHandler serviceHandler;
    private BaseValid<T> baseValid;

    public BaseResponse execute() throws Exception {
        if (baseRequest == null) {
            throw new Exception();
        }
        if (baseValid == null) {
            throw new Exception();
        }
        RequestTypeEnum requestType = null;
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
        Optional<T> optional = jpaRepository.findOne(specification);
        T result = (T) serviceHandler.handleService(optional.isPresent()?optional.get():null);
        SuccessResponse<T> response = SuccessResponse.<T>builder().build();
        response.setSuccess(true);
        response.setStatus(HttpStatus.OK);
        response.setData(result);
        return response;
    }

    private boolean requestValidation(BaseValid baseValid, RequestTypeEnum type) {
        if (type != null && baseValid != null) {
            BaseResponse errorMessage = null;
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

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the user is authenticated
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    public ServiceManager setRequest(BaseRequest baseRequest) {
        this.baseRequest = baseRequest;
        return this;
    }

    public ServiceManager setRepo(BaseRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
        return this;
    }

    public ServiceManager setSpec(Specification specification) {
        this.specification = specification;
        return this;
    }

    public ServiceManager setServiceHandle(ServiceHandler<T, BaseResponse> handler) {
        this.serviceHandler = handler;
        return this;
    }

    public ServiceManager setValid(BaseValid baseValid) {
        this.baseValid = baseValid;
        return this;
    }

    public interface ServiceHandler<T, U extends BaseResponse> {
        U handleService(T entity);
    }
}
