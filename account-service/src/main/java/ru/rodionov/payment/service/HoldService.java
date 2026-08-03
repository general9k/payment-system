package ru.rodionov.payment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rodionov.payment.contract.account.model.AccountStatusEnum;
import ru.rodionov.payment.contract.account.model.HoldStatusEnum;
import ru.rodionov.payment.contract.account.model.OperationTypeEnum;
import ru.rodionov.payment.contract.common.model.ErrorCode;
import ru.rodionov.payment.entity.Account;
import ru.rodionov.payment.entity.Hold;
import ru.rodionov.payment.entity.TransactionLog;
import ru.rodionov.starter.exception.LogicException;
import ru.rodionov.payment.repository.AccountRepository;
import ru.rodionov.payment.repository.HoldRepository;
import ru.rodionov.payment.repository.TransactionLogRepository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HoldService {

    private final HoldRepository holdRepository;
    private final AccountRepository accountRepository;
    private final TransactionLogRepository transactionLogRepository;

    @Transactional
    public Hold createHold(UUID accountId, BigDecimal amount, UUID paymentId, int ttlSeconds) {
        Account account = accountRepository.findByIdForUpdate(accountId)
                .orElseThrow(() -> new LogicException("Аккаунт не найден по id = %s".formatted(accountId), ErrorCode.NOT_FOUND));

        if (account.getStatus() != AccountStatusEnum.ACTIVE) {
            throw new IllegalStateException("Счёт не активен");
        }

        BigDecimal available = getAvailableBalance(account);
        if (available.compareTo(amount) < 0) {
            throw new LogicException("", ErrorCode.INTERNAL_ERROR); // TODO
//            throw new InsufficientFundsException(accountId, amount, available);
        }

        Hold hold = Hold.builder()
                .accountId(accountId)
                .amount(amount)
                .paymentId(paymentId)
                .expiresAt(OffsetDateTime.now().plusSeconds(ttlSeconds))
                .build();

        return holdRepository.save(hold);
    }

    @Transactional
    public Hold confirmHold(UUID holdId) {
        Hold hold = holdRepository.findById(holdId)
                .orElseThrow(() -> new IllegalArgumentException("Hold не найден: " + holdId));

        if (hold.getStatus() != HoldStatusEnum.PENDING) {
            throw new LogicException("", ErrorCode.INTERNAL_ERROR); // TODO
//            throw new HoldConflictException(holdId, hold.getStatus().name());
        }

        Account account = accountRepository.findByIdForUpdate(hold.getAccountId())
                .orElseThrow(() -> new LogicException("Аккаунт не найден по id = %s".formatted(hold.getAccountId()), ErrorCode.NOT_FOUND));

        account.setBalance(account.getBalance().subtract(hold.getAmount()));
        accountRepository.save(account);

        hold.setStatus(HoldStatusEnum.CONFIRMED);
        holdRepository.save(hold);

        writeLog(account, OperationTypeEnum.DEBIT, hold.getAmount(), "Списание по hold " + holdId);

        return hold;
    }

    @Transactional
    public Hold releaseHold(UUID holdId) {
        Hold hold = holdRepository.findById(holdId)
                .orElseThrow(() -> new IllegalArgumentException("Hold не найден: " + holdId));

        if (hold.getStatus() != HoldStatusEnum.PENDING) {
            throw new LogicException("", ErrorCode.INTERNAL_ERROR); // TODO
//            throw new HoldConflictException(holdId, hold.getStatus().name());
        }

        hold.setStatus(HoldStatusEnum.RELEASED);
        holdRepository.save(hold);

        return hold;
    }

    private BigDecimal getAvailableBalance(Account account) {
        BigDecimal heldAmount = holdRepository
                .findByAccountIdAndStatus(account.getId(), HoldStatusEnum.PENDING)
                .stream()
                .map(Hold::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return account.getBalance().subtract(heldAmount);
    }

    private void writeLog(Account account, OperationTypeEnum operation, BigDecimal amount, String reason) {
        TransactionLog log = TransactionLog.builder()
                .accountId(account.getId())
                .operationType(operation)
                .amount(amount)
                .balanceAfter(account.getBalance())
                .reason(reason)
                .build();
        transactionLogRepository.save(log);
    }
}