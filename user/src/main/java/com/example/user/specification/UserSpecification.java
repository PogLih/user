package com.example.user.specification;

import com.example.user.entity.User;
import com.example.user.request.BaseRequest;
import com.example.user.request.LoginRequest;
import com.example.user.request.SignUpRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class UserSpecification extends  BaseSpecification<User>{
    public Specification<User> getByName(BaseRequest baseRequest){
        SignUpRequest request = (SignUpRequest) baseRequest;

        Specification<User> spec = (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("username"),request.getUsername());
        };
        return spec;
    }

}
