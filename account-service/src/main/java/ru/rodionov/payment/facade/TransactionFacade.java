package ru.rodionov.payment.facade;

import org.springframework.data.domain.Pageable;
import ru.rodionov.payment.contract.account.model.TransactionPageResponse;

import java.util.UUID;

public interface TransactionFacade {

    TransactionPageResponse getTransactions(UUID accountId, Pageable pageable);
}
