package ru.rodionov.payment.converter;

import jakarta.persistence.Converter;
import ru.rodionov.payment.contract.account.model.HoldStatusEnum;
import ru.rodionov.starter.util.GenericEnumConverter;

@Converter
public class HoldStatusEnumConverter extends GenericEnumConverter<HoldStatusEnum, String> {

    public HoldStatusEnumConverter() {
        super(HoldStatusEnum.class);
    }
}
