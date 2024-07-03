package com.example.user.common.interfaces;

import com.example.user.common.enums.RequestTypeEnum;
import com.example.user.common.exception.ApplicationException;
import com.example.user.common.request.BaseRequest;
import com.example.user.common.response.BaseResponse;
import com.example.user.common.response.SuccessResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceManager<T> {
    private BaseRequest baseRequest;
    private Specification<T> specification;
    private ServiceHandler.WriteHandler<T> serviceHandler;
    private BaseValid<T> baseValid;


    public ServiceManager(BaseRequest request) {
        this.baseRequest = request;
    }


    public ServiceManager(Specification<T> specification) {
        this.specification = specification;
    }

    public ServiceManager(BaseValid<T> valid) {
        this.baseValid = valid;
    }

    public ServiceManager(ServiceHandler.WriteEntityHandler<T> serviceHandler) {
        this.serviceHandler = serviceHandler;
    }

    public BaseResponse execute() throws Exception {
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
                ((ServiceHandler.WriteEntityHandler<T>) serviceHandler).onChangeWriteEntityHandled(baseRequest);
        return new SuccessResponse(result);
    }

    private boolean requestValidation(BaseValid<T> baseValid, RequestTypeEnum type) {
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

}
