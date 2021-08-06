package com.playtomic.tests.wallet.dao;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.Objects;

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

    public Long getId() {
        return id;
    }

    public BigDecimal getCurrentBalance(){
        return currentBalance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WalletDao walletDao = (WalletDao) o;
        return Objects.equals(id, walletDao.id) &&
                currentBalance.equals(walletDao.currentBalance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, currentBalance);
    }
}
