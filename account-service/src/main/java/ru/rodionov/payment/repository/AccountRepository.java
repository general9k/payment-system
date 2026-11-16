package ru.rodionov.payment.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.rodionov.payment.contract.account.model.CurrencyEnum;
import ru.rodionov.payment.entity.Account;
import ru.rodionov.starter.repository.BaseRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends BaseRepository<Account, UUID> {

    List<Account> findByClientId(UUID clientId);

    boolean existsByClientIdAndCurrency(UUID clientId, CurrencyEnum currency);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE a.id = :id")
    Optional<Account> findByIdForUpdate(@Param("id") UUID id);
}