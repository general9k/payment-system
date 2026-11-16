package ru.rodionov.payment.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rodionov.payment.contract.account.model.AccountDTO;
import ru.rodionov.payment.contract.account.model.BalanceAdjustRequest;
import ru.rodionov.payment.contract.account.model.CreateAccountRequest;
import ru.rodionov.payment.contract.account.model.OperationTypeEnum;
import ru.rodionov.payment.contract.common.model.ErrorCode;
import ru.rodionov.payment.entity.Account;
import ru.rodionov.payment.facade.AccountFacade;
import ru.rodionov.payment.mapper.AccountMapper;
import ru.rodionov.payment.service.AccountService;
import ru.rodionov.payment.service.TransactionLogService;
import ru.rodionov.starter.exception.LogicBusinessException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountFacadeImpl implements AccountFacade {

    private final AccountService accountService;
    private final TransactionLogService transactionLogService;
    private final AccountMapper accountMapper;

    @Override
    @Transactional
    public AccountDTO createAccount(CreateAccountRequest request) {
        accountService.checkAccountDoesNotExists(request.getClientId(), request.getCurrency());
        Account account = accountService.save(accountMapper.toEntity(request));

        if (account.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            transactionLogService.writeLog(account, OperationTypeEnum.CREDIT, account.getBalance(), "Начальное пополнение");
        }
        return accountMapper.toDto(account);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountDTO getAccount(UUID id) {
        return accountMapper.toDto(accountService.getById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDTO> getAccountsByClient(UUID clientId) {
        return accountMapper.toDtoList(accountService.getByClientId(clientId));
    }

    @Override
    @Transactional
    public AccountDTO adjustBalance(UUID accountId, BalanceAdjustRequest request) {
        Account account = accountService.getById(accountId);
        accountService.checkActivityStatus(account);

        BigDecimal amount = request.getAmount();
        switch (request.getOperationType()) {
            case DEBIT -> { // списание
                if (account.getBalance().compareTo(amount) < 0) {
                    throw new LogicBusinessException(
                            String.format("Нельзя списать сумму большую, чем есть на балансе %s < %s", account.getBalance(), account),
                            ErrorCode.INTERNAL_ERROR);
                }
                account.setBalance(account.getBalance().subtract(amount));
            }
            case CREDIT -> account.setBalance(account.getBalance().add(amount));
            default ->
                    throw new LogicBusinessException("Недопустимая операция: " + request.getOperationType(), ErrorCode.INTERNAL_ERROR);
        }

        account = accountService.save(account);
        transactionLogService.writeLog(account, request.getOperationType(), amount, request.getReason());

        return accountMapper.toDto(account);
    }
}
