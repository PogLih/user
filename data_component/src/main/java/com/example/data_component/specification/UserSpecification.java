package com.example.data_component.specification;

import com.example.data_component.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class UserSpecification {

  public Specification<User> getByName(String name) {

    Specification<User> spec = (root, query, criteriaBuilder) -> {
      return criteriaBuilder.equal(root.get("username"), name);
    };
    return spec;
  }

}
