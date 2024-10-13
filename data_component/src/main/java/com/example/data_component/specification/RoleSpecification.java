package com.example.data_component.specification;

import com.example.data_component.entity.Role;
import jakarta.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Objects;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class RoleSpecification {

  public Specification<Role> getByName(String name) {
    Specification<Role> spec = (root, query, criteriaBuilder) -> {
      return criteriaBuilder.equal(root.get("name"), name);
    };
    return spec;
  }

  public Specification<Role> getByRoles(List<String> names) {
    return (root, query, criteriaBuilder) -> {
      CriteriaBuilder.In<String> inClause = criteriaBuilder.in(root.get("name"));
      for (String name : names) {
        if (Objects.nonNull(name)) {
          inClause.value(name);
        }
      }
      return inClause;
    };
  }
}
