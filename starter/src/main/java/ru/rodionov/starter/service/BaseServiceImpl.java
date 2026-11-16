package ru.rodionov.starter.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.rodionov.payment.contract.common.model.ErrorCode;
import ru.rodionov.starter.entity.BaseEntity;
import ru.rodionov.starter.exception.LogicBusinessException;
import ru.rodionov.starter.repository.BaseRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class BaseServiceImpl<E extends BaseEntity,
        ID extends Serializable,
        ER extends BaseRepository<E, ID>> implements BaseService<E, ID> {
    protected final ER repository;

    public BaseServiceImpl(final ER repository) {
        this.repository = repository;
    }

    @Override
    public E save(final E entity) {
        log.debug("Request to save entity : {}", entity);
        return repository.save(entity);
    }

    @Override
    public E update(final E entity) {
        log.debug("Request to update entity : {}", entity);
        return repository.save(entity);
    }

    @Override
    public Optional<E> findById(final ID id) {
        log.debug("Request to get entity : {}", id);
        return repository.findById(id);
    }

    public boolean existsById(final ID id) {
        return repository.existsById(id);
    }

    @Override
    public E delete(final ID id) {
        E entity = findById(id).orElseThrow(() ->
                new LogicBusinessException(String.format("Сущность с заданным идентификатором %s не найдена", id), ErrorCode.NOT_FOUND));
        log.debug("Request to delete entity : {}", id);
        repository.deleteById(id);

        return entity;
    }

    @Override
    public List<E> findAll() {
        log.debug("Request to get all entities");
        return repository.findAll();
    }

    @Override
    public Page<E> findAll(final Pageable pageable) {
        log.debug("Request to get all entities by pageable : {}", pageable);
        return repository.findAll(pageable);
    }

}