package com.example.base;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public abstract class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {

    private final BaseRepository<T> repository;

    @Override
    public T save(T t) {
        t = preSave(t);
        return repository.save(t);
    }

    public T preSave(T t) {
        return t;
    }

    @Override
    public T update(T t){
        return repository.save(t);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public Iterable<T> findAll(BooleanExpression exp, OrderSpecifier... orderBy) {
        return repository.findAll(exp, orderBy);
    }


    @Override
    public Optional<T> findOne(Long id) {
        return repository.findById(id);
    }

    @Override
    public void delete(T entity) {
        repository.delete(entity);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
