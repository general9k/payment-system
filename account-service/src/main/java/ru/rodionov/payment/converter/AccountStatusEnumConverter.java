package ru.rodionov.payment.converter;

import jakarta.persistence.Converter;
import ru.rodionov.payment.contract.account.model.AccountStatusEnum;
import ru.rodionov.starter.util.GenericEnumConverter;

@Converter
public class AccountStatusEnumConverter extends GenericEnumConverter<AccountStatusEnum, String> {

    public AccountStatusEnumConverter() {
        super(AccountStatusEnum.class);
    }
}
