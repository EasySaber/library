package com.example.sshomework.mappers;

import java.util.List;

/**
 * @author Aleksey Romodin
 */
public interface MapMapper<D, E> {
    D toDto(E e);

    E toEntity(D d);

    List<D> toDtoList(List<E> eList);

    List<E> toEntityList(List<D> dList);

}
