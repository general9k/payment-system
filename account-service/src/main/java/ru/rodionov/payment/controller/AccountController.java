package ru.rodionov.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.rodionov.payment.contract.account.api.AccountsApi;
import ru.rodionov.payment.contract.account.model.AccountResponse;
import ru.rodionov.payment.contract.account.model.CreateAccountRequest;
import ru.rodionov.payment.entity.Account;
import ru.rodionov.payment.mapper.AccountMapper;
import ru.rodionov.payment.service.AccountService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AccountController implements AccountsApi {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @Override
    public ResponseEntity<AccountResponse> createAccount(CreateAccountRequest request) {
        Account account = accountService.create(request.getClientId(), request.getCurrency(), request.getInitialBalance() != null
                ? request.getInitialBalance()
                : BigDecimal.ZERO
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountMapper.toResponse(account));
    }

    @Override
    public ResponseEntity<List<AccountResponse>> getAccountsByClient(UUID clientId) {
        List<AccountResponse> accounts = accountService.getByClientId(clientId).stream()
                .map(accountMapper::toResponse)
                .toList();
        return ResponseEntity.ok(accounts);
    }

    @Override
    public ResponseEntity<AccountResponse> getAccount(UUID accountId) {
        Account account = accountService.getById(accountId);
        return ResponseEntity.ok(accountMapper.toResponse(account));
    }
}