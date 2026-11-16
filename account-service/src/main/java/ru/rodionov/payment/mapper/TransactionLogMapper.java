package ru.rodionov.payment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.rodionov.payment.contract.account.model.TransactionDTO;
import ru.rodionov.payment.contract.account.model.TransactionPageResponse;
import ru.rodionov.payment.entity.TransactionLog;
import ru.rodionov.starter.mapper.BaseMapper;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionLogMapper extends BaseMapper<TransactionLog, TransactionDTO> {

    TransactionPageResponse toResponse(List<TransactionLog> content, Integer page, Integer size, Long totalCount);
}
