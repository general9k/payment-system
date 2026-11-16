package ru.rodionov.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.rodionov.payment.contract.account.api.HoldsApi;
import ru.rodionov.payment.contract.account.model.CreateHoldRequest;
import ru.rodionov.payment.contract.account.model.HoldDTO;
import ru.rodionov.payment.facade.HoldFacade;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class HoldController implements HoldsApi {

    private final HoldFacade facade;

    @Override
    public ResponseEntity<HoldDTO> createHold(UUID accountId, CreateHoldRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(facade.createHold(accountId, request));
    }

    @Override
    public ResponseEntity<HoldDTO> confirmHold(UUID holdId) {
        return ResponseEntity.ok(facade.confirmHold(holdId));
    }

    @Override
    public ResponseEntity<HoldDTO> releaseHold(UUID holdId) {
        return ResponseEntity.ok(facade.releaseHold(holdId));
    }
}