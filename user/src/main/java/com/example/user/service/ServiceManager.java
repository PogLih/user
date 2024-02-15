package com.example.user.service;

import com.example.user.annotation.RequestType;
import com.example.user.annotation.RequestTypeEnum;
import com.example.user.request.BaseRequest;
import com.example.user.response.BaseResponse;
import com.example.user.response.SuccessResponse;
import com.example.user.specification.BaseSpecification;
import com.example.user.valid.BaseValid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServiceManager {
    private BaseRequest baseRequest;
    private JpaRepository jpaRepository;
    private BaseSpecification specification;
    private ServiceHandler serviceHandler;
    private BaseValid baseValid;

    public ServiceManager setRequest(BaseRequest baseRequest) {
        this.baseRequest = baseRequest;
        return this;
    }

    public ServiceManager setRepo(JpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
        return this;
    }

    public ServiceManager setSpec(BaseSpecification specification) {
        this.specification = specification;
        return this;
    }

    public <T> ServiceManager setServiceHandle(ServiceHandler<T> handler) {
        this.serviceHandler = handler;
        return this;
    }

    public ServiceManager setValid(BaseValid baseValid){
        this.baseValid = baseValid;
        return this;
    }

    public BaseResponse execute() {
        Class<?> clazz = baseRequest.getClass();

        if (clazz.isAnnotationPresent(RequestType.class)) {
            RequestType annotation = clazz.getAnnotation(RequestType.class);
            RequestTypeEnum requestType = annotation.value();
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
                }case CHECK -> {
                    baseValid.validCheck(baseRequest);
                    break;
                }
            }
        }
        Object result = serviceHandler.handleService();
        this.jpaRepository.saveAndFlush(result);
        return new SuccessResponse<>().setData(result);
    }


    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the user is authenticated
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    public interface ServiceHandler<T> {
        T handleService();
    }
}
