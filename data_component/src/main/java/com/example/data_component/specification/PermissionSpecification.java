package com.example.data_component.specification;

import com.example.data_component.entity.Permission;
import jakarta.persistence.criteria.CriteriaBuilder;
import java.util.Objects;
import java.util.Set;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PermissionSpecification {

  public Specification<Permission> getByNames(Set<String> names) {
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

  public Specification<Permission> getByIds(Set<Long> ids) {
    return (root, query, criteriaBuilder) -> {
      CriteriaBuilder.In<Long> inClause = criteriaBuilder.in(root.get("ID"));
      for (Long id : ids) {
        if (Objects.nonNull(id)) {
          inClause.value(id);
        }
      }
      return inClause;
    };
  }
}
