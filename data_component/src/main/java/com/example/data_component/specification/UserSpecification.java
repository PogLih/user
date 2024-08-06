package com.example.data_component.specification;

import com.example.common_component.request.BaseRequest;
import com.example.common_component.request.SignUpRequest;
import com.example.data_component.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class UserSpecification extends BaseSpecification<User> {

  public Specification<User> getByName(BaseRequest baseRequest) {
    SignUpRequest request = (SignUpRequest) baseRequest;

    Specification<User> spec = (root, query, criteriaBuilder) -> {
      return criteriaBuilder.equal(root.get("username"), request.getUsername());
    };
    return spec;
  }

}
