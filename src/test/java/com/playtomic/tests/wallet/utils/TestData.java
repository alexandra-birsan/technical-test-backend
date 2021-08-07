package com.playtomic.tests.wallet.utils;

import com.playtomic.tests.wallet.dto.PaymentRequest;
import com.playtomic.tests.wallet.dto.TopUpRequest;

import java.math.BigDecimal;

public class TestData {

    public static final long WALLET_ID = 1L;
    public static final BigDecimal PAYMENT_AMOUNT = BigDecimal.valueOf(202.3);
    public static  final BigDecimal TOP_UP_AMOUNT = BigDecimal.TEN;
    public static final String CREDIT_CARD_NUMBER = "4242 4242 4242 4242";
    public static final BigDecimal CURRENT_BALANCE = BigDecimal.valueOf(200.2);

    public static PaymentRequest createPaymentRequest() {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(PAYMENT_AMOUNT);
        return paymentRequest;
    }

    public static TopUpRequest createTopUpRequest() {
        TopUpRequest request = new TopUpRequest();
        request.setAmount(TOP_UP_AMOUNT);
        request.setCreditCardNumber(CREDIT_CARD_NUMBER);
        return request;
    }
}
