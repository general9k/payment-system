package ru.rodionov.payment.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.rodionov.payment.contract.account.model.TransactionPageResponse;
import ru.rodionov.payment.entity.TransactionLog;
import ru.rodionov.payment.facade.TransactionFacade;
import ru.rodionov.payment.mapper.TransactionLogMapper;
import ru.rodionov.payment.service.TransactionLogService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionFacadeImpl implements TransactionFacade {

    private final TransactionLogService transactionLogService;
    private final TransactionLogMapper transactionLogMapper;

    @Override
    public TransactionPageResponse getTransactions(UUID accountId, Pageable pageable) {
        transactionLogService.checkExistsByAccountId(accountId);
        Page<TransactionLog> page = transactionLogService.findByAccountIdOrderByCreatedAtDesc(accountId, pageable);
        return transactionLogMapper.toResponse(page.getContent(), pageable.getPageNumber(), pageable.getPageSize(),
                page.getTotalElements());
    }
}
