package ru.rodionov.payment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.rodionov.payment.contract.account.model.OperationTypeEnum;
import ru.rodionov.payment.util.converter.OperationTypeEnumConverter;
import ru.rodionov.starter.entity.BaseEntity;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "transaction_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@EntityListeners({AuditingEntityListener.class})
public class TransactionLog extends BaseEntity {

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Convert(converter = OperationTypeEnumConverter.class)
    @Column(name = "operation_type", nullable = false, length = 20)
    private OperationTypeEnum operationType;

    @Column(name = "amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(name = "balance_after", nullable = false, precision = 19, scale = 4)
    private BigDecimal balanceAfter;

    @Column(name = "reason")
    private String reason;
}