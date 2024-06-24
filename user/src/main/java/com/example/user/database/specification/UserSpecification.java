package com.example.user.database.specification;

import com.example.user.common.entity.User;
import com.example.user.common.request.BaseRequest;
import com.example.user.common.request.SignUpRequest;
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
