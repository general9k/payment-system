package ru.rodionov.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rodionov.payment.contract.account.model.HoldStatusEnum;
import ru.rodionov.payment.entity.Hold;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface HoldRepository extends JpaRepository<Hold, UUID> {

    List<Hold> findByAccountIdAndStatus(UUID accountId, HoldStatusEnum status);

    List<Hold> findByStatusAndExpiresAtBefore(HoldStatusEnum status, OffsetDateTime now);
}
