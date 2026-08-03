package ru.rodionov.payment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rodionov.payment.contract.account.model.AccountStatusEnum;
import ru.rodionov.payment.contract.account.model.CurrencyEnum;
import ru.rodionov.payment.contract.account.model.OperationTypeEnum;
import ru.rodionov.payment.contract.common.model.ErrorCode;
import ru.rodionov.payment.entity.Account;
import ru.rodionov.payment.entity.TransactionLog;
import ru.rodionov.starter.exception.LogicException;
import ru.rodionov.payment.repository.AccountRepository;
import ru.rodionov.payment.repository.TransactionLogRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionLogRepository transactionLogRepository;

    @Transactional
    public Account create(UUID clientId, CurrencyEnum currency, BigDecimal initialBalance) {
        if (accountRepository.existsByClientIdAndCurrency(clientId, currency)) {
            throw new LogicException(
                    String.format("Счёт %s для клиента %s уже существует", currency, clientId), ErrorCode.CONFLICT);
        }

        Account account = Account.builder()
                .clientId(clientId)
                .currency(currency)
                .balance(initialBalance != null ? initialBalance : BigDecimal.ZERO)
                .build();

        account = accountRepository.save(account);

        if (account.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            writeLog(account, OperationTypeEnum.CREDIT, account.getBalance(), "Начальное пополнение");
        }

        return account;
    }

    @Transactional(readOnly = true)
    public Account getById(UUID accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new LogicException("Аккаунт не найден по id = %s".formatted(accountId), ErrorCode.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<Account> getByClientId(UUID clientId) {
        return accountRepository.findByClientId(clientId);
    }

    @Transactional
    public Account adjustBalance(UUID accountId, BigDecimal amount, OperationTypeEnum operation, String reason) {
        Account account = accountRepository.findByIdForUpdate(accountId)
                .orElseThrow(() -> new LogicException("Аккаунт не найден по id = %s".formatted(accountId), ErrorCode.NOT_FOUND));

        if (account.getStatus() != AccountStatusEnum.ACTIVE) {
            throw new IllegalStateException("Счёт не в статусе ACTIVE, операции запрещены");
        }

        switch (operation) {
            case DEBIT -> {
                if (account.getBalance().compareTo(amount) < 0) {
                    throw new LogicException("", ErrorCode.INTERNAL_ERROR); // TODO
//                    throw new InsufficientFundsException(accountId, amount, account.getBalance());
                }
                account.setBalance(account.getBalance().subtract(amount));
            }
            case CREDIT -> account.setBalance(account.getBalance().add(amount));
            default -> throw new IllegalArgumentException("Недопустимая операция: " + operation);
        }

        account = accountRepository.save(account);
        writeLog(account, operation, amount, reason);

        return account;
    }

    @Transactional(readOnly = true)
    public Page<TransactionLog> getTransactions(UUID accountId, int page, int size) {
        if (!accountRepository.existsById(accountId)) {
            throw new LogicException("Аккаунт не найден по id = %s".formatted(accountId), ErrorCode.NOT_FOUND);
        }
        return transactionLogRepository.findByAccountIdOrderByCreatedAtDesc(
                accountId, PageRequest.of(page, size));
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