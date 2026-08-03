package ru.rodionov.payment.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rodionov.payment.entity.TransactionLog;

import java.util.UUID;

@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, UUID> {

    Page<TransactionLog> findByAccountIdOrderByCreatedAtDesc(UUID accountId, Pageable pageable);
}