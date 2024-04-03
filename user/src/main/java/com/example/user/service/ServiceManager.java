package com.example.user.service;

import com.example.user.annotation.RequestType;
import com.example.user.annotation.RequestTypeEnum;
import com.example.user.repository.BaseRepository;
import com.example.user.request.BaseRequest;
import com.example.user.response.BaseResponse;
import com.example.user.response.SuccessResponse;
import com.example.user.specification.BaseSpecification;
import com.example.user.valid.BaseValid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContextException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ServiceManager<T> {
    private BaseRequest baseRequest;
    private BaseRepository jpaRepository;
    private Specification specification;
    private ServiceHandler serviceHandler;
    private BaseValid baseValid;

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

    public BaseResponse execute() throws Exception {
        if (baseRequest == null) {
            throw new Exception();
        }
        if (baseValid == null) {
            throw new Exception();
        }
        RequestTypeEnum requestType = null;
        Class<?> clazz = baseRequest.getClass();
        if (clazz.isAnnotationPresent(RequestType.class)) {
            RequestType annotation = clazz.getAnnotation(RequestType.class);
            requestType = annotation.value();
            switch (requestType) {
                case GET -> {
                    baseValid.validGet(baseRequest);
                    break;
                }
                case INSERT -> {
                    baseValid.validInsert(baseRequest);
                    break;
                }
                case DELETE -> {
                    baseValid.validDelete(baseRequest);
                    break;
                }
                case UPDATE -> {
                    baseValid.validUpdate(baseRequest);
                    break;
                }
                case CHECK -> {
                    baseValid.validCheck(baseRequest);
                    break;
                }
            }
        }
        if(requestType.equals(RequestTypeEnum.GET) || requestType.equals(RequestTypeEnum.CHECK)
         || requestType.equals(RequestTypeEnum.GET_LIST)){
            if(requestType.equals(RequestTypeEnum.GET_LIST)){
                List all = jpaRepository.findAll(specification);
            }else{
                Optional one = jpaRepository.findOne(specification);
            }
        }else{
            Object result = serviceHandler.handleService();
            if (jpaRepository == null) {
                throw new Exception();
            }
            this.jpaRepository.saveAndFlush(result);
        }
        T result  = (T) serviceHandler.handleService();
        return new SuccessResponse<T>().setData(result);
    }


    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the user is authenticated
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    public interface ServiceHandler<T, U extends BaseResponse> {
        T handleService();
    }
}
