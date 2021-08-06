package com.playtomic.tests.wallet.dto;


import java.math.BigDecimal;
import java.util.Objects;

public class WalletDto {

    private Long id;
    private BigDecimal currentBalance;

    public WalletDto(Long id, BigDecimal currentBalance) {
        this.id = id;
        this.currentBalance = currentBalance;
    }

    public WalletDto() {
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WalletDto walletDto = (WalletDto) o;
        return Objects.equals(id, walletDto.id) &&
                Objects.equals(currentBalance, walletDto.currentBalance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, currentBalance);
    }
}
