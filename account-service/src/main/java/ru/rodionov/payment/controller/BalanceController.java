package ru.rodionov.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.rodionov.payment.contract.account.api.BalanceApi;
import ru.rodionov.payment.contract.account.model.AccountResponse;
import ru.rodionov.payment.contract.account.model.BalanceAdjustRequest;
import ru.rodionov.payment.entity.Account;
import ru.rodionov.payment.mapper.AccountMapper;
import ru.rodionov.payment.service.AccountService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BalanceController implements BalanceApi {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @Override
    public ResponseEntity<AccountResponse> adjustBalance(UUID accountId, BalanceAdjustRequest request) {
        Account account = accountService.adjustBalance(
                accountId,
                request.getAmount(),
                request.getOperationType(),
                request.getReason()
        );
        return ResponseEntity.ok(accountMapper.toResponse(account));
    }
}
