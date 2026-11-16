package ru.rodionov.payment.service;

import org.springframework.stereotype.Service;
import ru.rodionov.payment.contract.account.model.AccountStatusEnum;
import ru.rodionov.payment.contract.account.model.CurrencyEnum;
import ru.rodionov.payment.contract.common.model.ErrorCode;
import ru.rodionov.payment.entity.Account;
import ru.rodionov.payment.repository.AccountRepository;
import ru.rodionov.starter.exception.LogicBusinessException;
import ru.rodionov.starter.service.BaseServiceImpl;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService extends BaseServiceImpl<Account, UUID, AccountRepository> {

    public AccountService(final AccountRepository repository) {
        super(repository);
    }

    public Account getById(UUID id) {
        return repository.findById(id).orElseThrow(() ->
                new LogicBusinessException(String.format("Счет %s не существует.", id), ErrorCode.NOT_FOUND));
    }

    public List<Account> getByClientId(UUID clientId) {
        return repository.findByClientId(clientId);
    }

    public void checkAccountDoesNotExists(UUID clientId, CurrencyEnum currency) {
        if (repository.existsByClientIdAndCurrency(clientId, currency)) {
            throw new LogicBusinessException(
                    String.format("Счёт %s для клиента %s уже существует", currency, clientId), ErrorCode.CONFLICT);
        }
    }

    public void checkActivityStatus(Account account) {
        if (!AccountStatusEnum.ACTIVE.equals(account.getStatus())) {
            throw new LogicBusinessException("Счёт не активен", ErrorCode.CONFLICT);
        }
    }
}