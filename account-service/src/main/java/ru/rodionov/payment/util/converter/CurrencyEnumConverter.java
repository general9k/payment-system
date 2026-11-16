package ru.rodionov.payment.util.converter;

import jakarta.persistence.Converter;
import ru.rodionov.payment.contract.account.model.CurrencyEnum;
import ru.rodionov.starter.util.GenericEnumConverter;

@Converter
public class CurrencyEnumConverter extends GenericEnumConverter<CurrencyEnum, String> {

    public CurrencyEnumConverter() {
        super(CurrencyEnum.class);
    }
}
