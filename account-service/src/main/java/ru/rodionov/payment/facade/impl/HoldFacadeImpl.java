package ru.rodionov.payment.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rodionov.payment.contract.account.model.CreateHoldRequest;
import ru.rodionov.payment.contract.account.model.HoldDTO;
import ru.rodionov.payment.contract.account.model.HoldStatusEnum;
import ru.rodionov.payment.contract.account.model.OperationTypeEnum;
import ru.rodionov.payment.contract.common.model.ErrorCode;
import ru.rodionov.payment.entity.Account;
import ru.rodionov.payment.entity.Hold;
import ru.rodionov.payment.facade.HoldFacade;
import ru.rodionov.payment.mapper.HoldMapper;
import ru.rodionov.payment.service.AccountService;
import ru.rodionov.payment.service.HoldService;
import ru.rodionov.payment.service.TransactionLogService;
import ru.rodionov.starter.exception.LogicBusinessException;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HoldFacadeImpl implements HoldFacade {

    private final AccountService accountService;
    private final HoldService holdService;
    private final TransactionLogService transactionLogService;
    private final HoldMapper holdMapper;

    @Override
    @Transactional
    public HoldDTO createHold(UUID accountId, CreateHoldRequest request) {
        Account account = accountService.getById(accountId);
        accountService.checkActivityStatus(account);

        BigDecimal available = getAvailableBalance(account);
        if (available.compareTo(request.getAmount()) < 0) {
            throw new LogicBusinessException("Недостаточно средств.", ErrorCode.UNPROCESSABLE_ENTITY);
        }

        Hold hold = holdMapper.toEntity(request);
        return holdMapper.toDto(holdService.save(hold));
    }

    @Override
    @Transactional
    public HoldDTO confirmHold(UUID holdId) {
        Hold hold = holdService.getById(holdId);

        if (hold.getStatus() != HoldStatusEnum.PENDING) {
            throw new LogicBusinessException("", ErrorCode.INTERNAL_ERROR); // TODO
//            throw new HoldConflictException(holdId, hold.getStatus().name());
        }

        Account account = hold.getAccount();

        account.setBalance(account.getBalance().subtract(hold.getAmount()));
        accountService.save(account);

        hold.setStatus(HoldStatusEnum.CONFIRMED);
        holdService.save(hold);

        transactionLogService.writeLog(account, OperationTypeEnum.DEBIT, hold.getAmount(), "Списание по hold " + holdId);

        return holdMapper.toDto(hold);
    }

    @Override
    @Transactional
    public HoldDTO releaseHold(UUID holdId) {
        Hold hold = holdService.findById(holdId)
                .orElseThrow(() -> new IllegalArgumentException("Hold не найден: " + holdId));

        if (hold.getStatus() != HoldStatusEnum.PENDING) {
            throw new LogicBusinessException("", ErrorCode.INTERNAL_ERROR); // TODO
        }

        hold.setStatus(HoldStatusEnum.RELEASED);
        holdService.save(hold);

        return holdMapper.toDto(hold);
    }

    private BigDecimal getAvailableBalance(Account account) { // разница текущего баланса и холдов в обработке
        return account.getBalance().subtract(holdService.sumAmountByAccountIdAndStatus(account.getId(), HoldStatusEnum.PENDING));
    }
}
