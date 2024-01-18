package com.example.user.specification;

import org.springframework.data.jpa.domain.Specification;

public abstract class BaseSpecification<T>{
    protected Specification<T> distinct() {
        return (root, query, cb) -> {
            query.distinct(true);
            return null;
        };
    }
}
