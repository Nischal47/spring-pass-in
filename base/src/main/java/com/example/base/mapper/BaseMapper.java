package com.example.base.mapper;

import java.util.List;

public interface BaseMapper<D, E> {

    E toEntity(D dto);

    D toDto(E entity);

    List<E> toEntity(Iterable<D> dtoList);

    List<D> toDto(Iterable<E> entityList);
}

