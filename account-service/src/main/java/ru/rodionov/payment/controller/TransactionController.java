package ru.rodionov.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.rodionov.payment.contract.account.api.TransactionsApi;
import ru.rodionov.payment.contract.account.model.TransactionPageResponse;
import ru.rodionov.payment.facade.TransactionFacade;
import ru.rodionov.starter.util.PageUtils;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TransactionController implements TransactionsApi {

    private final TransactionFacade facade;

    @Override
    public ResponseEntity<TransactionPageResponse> getTransactions(UUID accountId, Integer page, Integer size) {
        return ResponseEntity.ok(facade.getTransactions(accountId, PageUtils.pageable(page, size, null)));
    }
}
