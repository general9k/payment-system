package ru.rodionov.payment.service;

import org.springframework.stereotype.Service;
import ru.rodionov.payment.contract.account.model.HoldStatusEnum;
import ru.rodionov.payment.contract.common.model.ErrorCode;
import ru.rodionov.payment.entity.Hold;
import ru.rodionov.payment.repository.HoldRepository;
import ru.rodionov.starter.exception.LogicBusinessException;
import ru.rodionov.starter.service.BaseServiceImpl;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class HoldService extends BaseServiceImpl<Hold, UUID, HoldRepository> {

    public HoldService(final HoldRepository repository) {
        super(repository);
    }

    public Hold getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new LogicBusinessException("Hold не найден: " + id, ErrorCode.NOT_FOUND));
    }

    public void checkIsPending(Hold hold) {
        if (HoldStatusEnum.PENDING.equals(hold.getStatus())) {
            throw new LogicBusinessException("Hold не в состоянии ожидания.", ErrorCode.INTERNAL_ERROR);
        }
    }

    public BigDecimal sumAmountByAccountIdAndStatus(UUID accountId, HoldStatusEnum status) {
        return repository.sumAmountByAccountIdAndStatus(accountId, status);
    }

    public List<Hold> findByStatusAndExpiresAtBefore(HoldStatusEnum status, OffsetDateTime now) {
        return repository.findByStatusAndExpiresAtBefore(status, now);
    }
}