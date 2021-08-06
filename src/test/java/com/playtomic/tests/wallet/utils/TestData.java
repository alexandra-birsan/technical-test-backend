package com.playtomic.tests.wallet.utils;

import com.playtomic.tests.wallet.dto.ChargeRequest;

import java.math.BigDecimal;

public class TestData {

    public static final long WALLET_ID = 1L;
    public static final double CHARGE_AMOUNT = 202.3;
    public static  final BigDecimal RECHARGE_AMOUNT = BigDecimal.TEN;
    public static final String CREDIT_CARD_NUMBER = "4242 4242 4242 4242";
    public static final BigDecimal CURRENT_BALANCE = BigDecimal.valueOf(200.2);

    public static ChargeRequest createChargeRequest() {
        ChargeRequest chargeRequest = new ChargeRequest();
        chargeRequest.setAmount(BigDecimal.valueOf(CHARGE_AMOUNT));
        return chargeRequest;
    }
}
