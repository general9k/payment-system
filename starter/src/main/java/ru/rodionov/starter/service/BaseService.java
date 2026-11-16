package ru.rodionov.starter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.rodionov.starter.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BaseService<E extends BaseEntity, ID extends Serializable> {
    E save(E entity);

    E update(E entity);

    Optional<E> findById(ID id);

    boolean existsById(final ID id);

    E delete(ID id);

    List<E> findAll();

    Page<E> findAll(Pageable pageable);
}