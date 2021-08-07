package com.playtomic.tests.wallet.service.impl;


import com.playtomic.tests.wallet.exception.StripeServiceException;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static com.playtomic.tests.wallet.utils.TestData.CREDIT_CARD_NUMBER;

public class DefaultStripeServiceTest {

    private DefaultStripeService stripeService = new DefaultStripeService();

    @Test
    public void chargeWhenInvalidAmount() {
        sendPaymentRequest(5)
                .expectError(StripeServiceException.class)
                .verify();
    }

    @Test
    public void chargeSuccessfully() {
        sendPaymentRequest(15)
                .expectNext(true)
                .verifyComplete();
    }

    private StepVerifier.FirstStep<Boolean> sendPaymentRequest(int chargeAmount) {
        return StepVerifier.create(stripeService.charge(CREDIT_CARD_NUMBER, new BigDecimal(chargeAmount)));
    }
}
