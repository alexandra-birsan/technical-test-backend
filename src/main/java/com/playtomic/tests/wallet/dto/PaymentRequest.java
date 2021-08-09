package com.playtomic.tests.wallet.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@EqualsAndHashCode
@Getter
@Setter
public class PaymentRequest {

    @Positive
    @NotNull
    private BigDecimal amount;
}
