package com.playtomic.tests.wallet.dto;

import com.playtomic.tests.wallet.utils.ValidationUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PaymentRequestTest {

    private ValidationUtils<PaymentRequest> validationUtils = new ValidationUtils<>();

    @ParameterizedTest
    @ValueSource(doubles = {-2.4, 0.0})
    @NullSource
    void isInvalidAmount(Double chargeAmount) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(Optional.ofNullable(chargeAmount).map(BigDecimal::valueOf).orElse(null));

        assertFalse(validationUtils.isValid(paymentRequest));
    }

    @ParameterizedTest
    @ValueSource(doubles = {10000, 12.5, 0.01})
    void isValidAmount(Double chargeAmount) {
        PaymentRequest paymentRequest = createPaymentRequest(chargeAmount);

        assertTrue(validationUtils.isValid(paymentRequest));
    }

    private PaymentRequest createPaymentRequest(Double chargeAmount) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(BigDecimal.valueOf(chargeAmount));
        return paymentRequest;
    }
}
