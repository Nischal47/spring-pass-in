package com.example.base;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.List;
import java.util.Optional;

public interface BaseService<T extends BaseEntity> {

    T save(T t);

    T update(T t);

    List<T> findAll();

    Iterable<T> findAll(BooleanExpression exp, OrderSpecifier... orderBy);

    Optional<T> findOne(Long id);

    void delete(T t);

    void delete(Long id);

}
