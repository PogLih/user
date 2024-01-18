package com.example.user.specification;

import com.example.user.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class UserSpecification extends  BaseSpecification<User>{
    public Specification<User> getByName(String name){
        Specification<User> spec = (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("username"),name);
        };
        return spec;
    }
}
