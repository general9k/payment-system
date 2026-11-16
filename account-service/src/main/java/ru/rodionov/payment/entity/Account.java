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
import ru.rodionov.payment.contract.account.model.AccountStatusEnum;
import ru.rodionov.payment.contract.account.model.CurrencyEnum;
import ru.rodionov.payment.util.converter.AccountStatusEnumConverter;
import ru.rodionov.payment.util.converter.CurrencyEnumConverter;
import ru.rodionov.starter.entity.BaseEntity;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@EntityListeners({AuditingEntityListener.class})
public class Account extends BaseEntity {

    @Column(name = "client_id", nullable = false)
    private UUID clientId;

    @Convert(converter = CurrencyEnumConverter.class)
    @Column(name = "currency", nullable = false, length = 3)
    private CurrencyEnum currency;

    @Builder.Default
    @Column(name = "balance", nullable = false, precision = 19, scale = 4)
    private BigDecimal balance = BigDecimal.ZERO;

    @Builder.Default
    @Convert(converter = AccountStatusEnumConverter.class)
    @Column(name = "status", nullable = false, length = 20)
    private AccountStatusEnum status = AccountStatusEnum.ACTIVE;
}