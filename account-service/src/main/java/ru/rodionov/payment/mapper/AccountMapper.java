package ru.rodionov.payment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.rodionov.payment.contract.account.model.AccountDTO;
import ru.rodionov.payment.contract.account.model.CreateAccountRequest;
import ru.rodionov.payment.entity.Account;
import ru.rodionov.starter.mapper.BaseMapper;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountMapper extends BaseMapper<Account, AccountDTO> {

    Account toEntity(CreateAccountRequest accountDTO);
}