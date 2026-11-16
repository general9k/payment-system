package ru.rodionov.payment.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.rodionov.payment.contract.account.model.HoldStatusEnum;
import ru.rodionov.payment.entity.Hold;
import ru.rodionov.starter.repository.BaseRepository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface HoldRepository extends BaseRepository<Hold, UUID> {

    @Query("SELECT COALESCE(SUM(h.amount), 0) FROM Hold h WHERE h.accountId = :accountId AND h.status = :status")
    BigDecimal sumAmountByAccountIdAndStatus(@Param("accountId") UUID accountId,
                                             @Param("status") HoldStatusEnum status);

    List<Hold> findByStatusAndExpiresAtBefore(HoldStatusEnum status, OffsetDateTime now);
}
