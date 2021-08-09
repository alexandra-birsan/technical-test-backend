package com.playtomic.tests.wallet.service;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;

/**
 * Handles the communication with Stripe.
 */
public interface StripeService {

    /**
     * @param creditCardNumber The number of the credit card
     * @param amount           The amount that will be charged.
     * @return true if the amount was charged successfully
     */
    Mono<Boolean> charge(String creditCardNumber, BigDecimal amount);

}
