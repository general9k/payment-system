package ru.rodionov.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.rodionov.payment.contract.account.api.AccountsApi;
import ru.rodionov.payment.contract.account.api.BalanceApi;
import ru.rodionov.payment.contract.account.model.AccountDTO;
import ru.rodionov.payment.contract.account.model.BalanceAdjustRequest;
import ru.rodionov.payment.contract.account.model.CreateAccountRequest;
import ru.rodionov.payment.facade.AccountFacade;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AccountController implements AccountsApi, BalanceApi {

    private final AccountFacade facade;

    @Override
    public ResponseEntity<AccountDTO> createAccount(CreateAccountRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(facade.createAccount(request));
    }

    @Override
    public ResponseEntity<List<AccountDTO>> getAccountsByClient(UUID clientId) {
        return ResponseEntity.ok(facade.getAccountsByClient(clientId));
    }

    @Override
    public ResponseEntity<AccountDTO> getAccount(UUID accountId) {
        return ResponseEntity.ok(facade.getAccount(accountId));
    }

    @Override
    public ResponseEntity<AccountDTO> adjustBalance(UUID accountId, BalanceAdjustRequest request) {
        return ResponseEntity.ok(facade.adjustBalance(accountId, request));
    }
}