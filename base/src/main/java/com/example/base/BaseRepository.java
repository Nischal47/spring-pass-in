package com.example.base;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, Long>, QuerydslPredicateExecutor<T> {

}
