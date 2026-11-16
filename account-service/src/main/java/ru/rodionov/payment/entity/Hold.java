package ru.rodionov.payment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.rodionov.payment.contract.account.model.HoldStatusEnum;
import ru.rodionov.payment.util.converter.HoldStatusEnumConverter;
import ru.rodionov.starter.entity.BaseEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "holds")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@EntityListeners({AuditingEntityListener.class})
public class Hold extends BaseEntity {

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private Account account;

    @Column(name = "amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Builder.Default
    @Convert(converter = HoldStatusEnumConverter.class)
    @Column(name = "status", nullable = false, length = 20)
    private HoldStatusEnum status = HoldStatusEnum.PENDING;

    @Column(name = "payment_id", nullable = false)
    private UUID paymentId;

    @Column(name = "expires_at", nullable = false)
    private OffsetDateTime expiresAt;

}