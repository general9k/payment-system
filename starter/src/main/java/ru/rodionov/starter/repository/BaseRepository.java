package ru.rodionov.starter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import ru.rodionov.starter.entity.BaseEntity;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseRepository<E extends BaseEntity, ID extends Serializable>
        extends JpaRepository<E, ID>, JpaSpecificationExecutor<E>, QuerydslPredicateExecutor<E> {
}
