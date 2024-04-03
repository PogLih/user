package com.example.user.specification;

import com.example.user.entity.Role;
import com.example.user.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class RoleSpecification extends  BaseSpecification<Role>{
    public Specification<Role> getByName(String name){
        Specification<Role> spec = (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("name"),name);
        };
        return spec;
    }
}
