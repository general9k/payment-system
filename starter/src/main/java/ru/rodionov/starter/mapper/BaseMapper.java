package ru.rodionov.starter.mapper;

import ru.rodionov.payment.contract.common.model.PageInfo;
import ru.rodionov.starter.entity.BaseEntity;

import java.util.List;

public interface BaseMapper<E extends BaseEntity, DTO> {

    E toEntity(DTO dto);

    DTO toDto(E entity);

    List<DTO> toDtoList(List<E> entities);

    PageInfo toPageInfo(Integer page, Integer size, Long totalCount);
}
