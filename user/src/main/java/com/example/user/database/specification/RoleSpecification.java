package com.example.user.database.specification;

import com.example.user.common.entity.Role;
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
