package ru.rodionov.payment.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.rodionov.payment.entity.TransactionLog;
import ru.rodionov.starter.repository.BaseRepository;

import java.util.UUID;

@Repository
public interface TransactionLogRepository extends BaseRepository<TransactionLog, UUID> {

    Page<TransactionLog> findByAccountIdOrderByCreatedAtDesc(UUID accountId, Pageable pageable);
}