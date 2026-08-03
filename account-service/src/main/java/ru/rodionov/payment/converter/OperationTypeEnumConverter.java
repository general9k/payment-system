package ru.rodionov.payment.converter;

import jakarta.persistence.Converter;
import ru.rodionov.payment.contract.account.model.OperationTypeEnum;
import ru.rodionov.starter.util.GenericEnumConverter;

@Converter
public class OperationTypeEnumConverter extends GenericEnumConverter<OperationTypeEnum, String> {

    public OperationTypeEnumConverter() {
        super(OperationTypeEnum.class);
    }
}
