package ru.rodionov.starter.exception;

import lombok.Getter;
import ru.rodionov.payment.contract.common.model.ErrorCode;

@Getter
public class LogicBusinessException extends RuntimeException {

    private final String message;
    private final ErrorCode code;

    public LogicBusinessException(String message, ErrorCode code) {
        super(message);
        this.message = message;
        this.code = code;
    }
}
