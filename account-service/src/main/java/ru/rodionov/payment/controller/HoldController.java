package ru.rodionov.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.rodionov.payment.contract.account.api.HoldsApi;
import ru.rodionov.payment.contract.account.model.CreateHoldRequest;
import ru.rodionov.payment.contract.account.model.HoldResponse;
import ru.rodionov.payment.entity.Hold;
import ru.rodionov.payment.mapper.AccountMapper;
import ru.rodionov.payment.service.HoldService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class HoldController implements HoldsApi {

    private final HoldService holdService;
    private final AccountMapper accountMapper;

    @Override
    public ResponseEntity<HoldResponse> createHold(UUID accountId, CreateHoldRequest request) {
        Hold hold = holdService.createHold(
                accountId,
                request.getAmount(),
                request.getPaymentId(),
                request.getTtlSeconds() != null ? request.getTtlSeconds() : 300
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountMapper.toResponse(hold));
    }

    @Override
    public ResponseEntity<HoldResponse> confirmHold(UUID holdId) {
        Hold hold = holdService.confirmHold(holdId);
        return ResponseEntity.ok(accountMapper.toResponse(hold));
    }

    @Override
    public ResponseEntity<HoldResponse> releaseHold(UUID holdId) {
        Hold hold = holdService.releaseHold(holdId);
        return ResponseEntity.ok(accountMapper.toResponse(hold));
    }
}