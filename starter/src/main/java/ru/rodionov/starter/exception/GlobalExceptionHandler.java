package ru.rodionov.starter.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.rodionov.payment.contract.common.model.ErrorResponse;
import ru.rodionov.starter.util.DateUtil;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LogicBusinessException.class)
    public ResponseEntity<ErrorResponse> handleLogicException(LogicBusinessException ex,
                                                              HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse()
                .code(ex.getCode().name())
                .message(ex.getMessage())
                .timestamp(DateUtil.truncatedNow())
                .path(request.getRequestURI());

        return ResponseEntity.status(ex.getCode().getValue()).body(error);
    }
}