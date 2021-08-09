package com.playtomic.tests.wallet.service.impl;

import com.playtomic.tests.wallet.exception.StripeServiceException;
import com.playtomic.tests.wallet.service.StripeService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;


/**
 * Handles the communication with Stripe.
 * <p>
 * A real implementation would call to String using their API/SDK.
 * This dummy implementation throws an error when trying to charge less than 10€.
 */
@Service
public class DefaultStripeService implements StripeService {

    private static final BigDecimal THRESHOLD = new BigDecimal(10);

    /**
     * Charges money in the credit card.
     * <p>
     * Ignore the fact that no CVC or expiration date are provided.
     *
     * @param creditCardNumber The number of the credit card
     * @param amount           The amount that will be charged.
     */
    @Override
    public Mono<Boolean> charge(String creditCardNumber, BigDecimal amount) {
        Assert.notNull(creditCardNumber, "creditCardNumber == null");
        Assert.notNull(amount, "amount == null");

        if (amount.compareTo(THRESHOLD) < 0) {
            return Mono.error(new StripeServiceException());
        }
        return Mono.just(true);
    }
}
