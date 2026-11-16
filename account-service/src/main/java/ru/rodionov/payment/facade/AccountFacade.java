package ru.rodionov.payment.facade;

import ru.rodionov.payment.contract.account.model.AccountDTO;
import ru.rodionov.payment.contract.account.model.BalanceAdjustRequest;
import ru.rodionov.payment.contract.account.model.CreateAccountRequest;

import java.util.List;
import java.util.UUID;

public interface AccountFacade {

    AccountDTO createAccount(CreateAccountRequest request);

    AccountDTO getAccount(UUID id);

    List<AccountDTO> getAccountsByClient(UUID clientId);

    AccountDTO adjustBalance(UUID accountId, BalanceAdjustRequest request);
}
