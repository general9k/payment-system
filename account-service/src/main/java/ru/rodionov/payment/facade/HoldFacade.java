package ru.rodionov.payment.facade;

import ru.rodionov.payment.contract.account.model.CreateHoldRequest;
import ru.rodionov.payment.contract.account.model.HoldDTO;

import java.util.UUID;

public interface HoldFacade {

    HoldDTO createHold(UUID accountId, CreateHoldRequest request);

    HoldDTO confirmHold(UUID holdId);

    HoldDTO releaseHold(UUID holdId);
}
