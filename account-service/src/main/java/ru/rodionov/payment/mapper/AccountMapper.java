package ru.rodionov.payment.mapper;

import org.mapstruct.Mapper;
import ru.rodionov.payment.contract.account.model.AccountResponse;
import ru.rodionov.payment.contract.account.model.HoldResponse;
import ru.rodionov.payment.contract.account.model.TransactionResponse;
import ru.rodionov.payment.entity.Account;
import ru.rodionov.payment.entity.Hold;
import ru.rodionov.payment.entity.TransactionLog;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountResponse toResponse(Account account);

    HoldResponse toResponse(Hold hold);

    TransactionResponse toResponse(TransactionLog log);
}