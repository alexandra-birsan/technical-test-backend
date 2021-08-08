package com.playtomic.tests.wallet.dao;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

@EqualsAndHashCode
@Getter
@ToString
@Table(value = "wallet")
public class WalletDao {

    @Id
    private Long id;
    @NonNull
    private BigDecimal currentBalance;

    public WalletDao(Long id, BigDecimal currentBalance) {
        this.id = id;
        this.currentBalance = currentBalance;
    }
}
