package ru.rodionov.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.rodionov.payment.contract.account.api.TransactionsApi;
import ru.rodionov.payment.contract.account.model.TransactionPageResponse;
import ru.rodionov.payment.entity.TransactionLog;
import ru.rodionov.payment.mapper.AccountMapper;
import ru.rodionov.payment.service.AccountService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TransactionController implements TransactionsApi {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @Override
    public ResponseEntity<TransactionPageResponse> getTransactions(UUID accountId, Integer page, Integer size) {
        Page<TransactionLog> result = accountService.getTransactions(
                accountId,
                page != null ? page : 0,
                size != null ? size : 20
        );

        TransactionPageResponse response = new TransactionPageResponse();
        response.setContent(result.getContent().stream()
                .map(accountMapper::toResponse)
                .toList());
        response.setPage(result.getNumber());
        response.setSize(result.getSize());
        response.setTotalElements(result.getTotalElements());
        response.setTotalPages(result.getTotalPages());

        return ResponseEntity.ok(response);
    }
}
