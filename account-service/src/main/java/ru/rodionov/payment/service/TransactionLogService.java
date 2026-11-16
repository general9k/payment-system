package ru.rodionov.payment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.rodionov.payment.contract.account.model.OperationTypeEnum;
import ru.rodionov.payment.contract.common.model.ErrorCode;
import ru.rodionov.payment.entity.Account;
import ru.rodionov.payment.entity.TransactionLog;
import ru.rodionov.payment.repository.TransactionLogRepository;
import ru.rodionov.starter.exception.LogicBusinessException;
import ru.rodionov.starter.service.BaseServiceImpl;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class TransactionLogService extends BaseServiceImpl<TransactionLog, UUID, TransactionLogRepository> {

    public TransactionLogService(TransactionLogRepository repository) {
        super(repository);
    }

    public void checkExistsByAccountId(UUID accountId) {
        if (!repository.existsById(accountId)) {
            throw new LogicBusinessException("Аккаунт не найден по id = %s".formatted(accountId), ErrorCode.NOT_FOUND);
        }
    }

    public Page<TransactionLog> findByAccountIdOrderByCreatedAtDesc(UUID accountId, Pageable pageable) {
        return repository.findByAccountIdOrderByCreatedAtDesc(accountId, pageable);
    }

    public void writeLog(Account account, OperationTypeEnum operation, BigDecimal amount, String reason) {
        TransactionLog log = TransactionLog.builder()
                .accountId(account.getId())
                .operationType(operation)
                .amount(amount)
                .balanceAfter(account.getBalance())
                .reason(reason)
                .build();
        super.save(log);
    }
}
