package com.playtomic.tests.wallet.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Objects;

public class RechargeRequest {

    @NotBlank
    private String creditCardNumber;
    @Positive
    @NotNull
    private BigDecimal amount;

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public RechargeRequest setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
        return this;
    }

    public RechargeRequest setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RechargeRequest that = (RechargeRequest) o;
        return Objects.equals(creditCardNumber, that.creditCardNumber) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creditCardNumber, amount);
    }
}
