package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.dto.ChargeRequest;
import com.playtomic.tests.wallet.dto.RechargeRequest;
import com.playtomic.tests.wallet.dto.WalletDto;
import reactor.core.publisher.Mono;

/**
 * Handles the operations applied to the wallet objects
 */
public interface WalletService {

    /**
     * Performs a payment on the specified wallet
     *
     * @param walletId The id of the wallet to which the payment should be applied
     * @param request  The request body containing the payment amount
     * @return The updated wallet
     */
    Mono<WalletDto> charge(Long walletId, ChargeRequest request);

    /**
     * Performs a top-up on the specified wallet
     *
     * @param walletId The id of the wallet to which the top-up should be applied
     * @param request  The request body containing the top-up amount
     * @return The updated wallet
     */
    Mono<WalletDto> recharge(Long walletId, RechargeRequest request);

    /**
     * @param walletId The id of the wallet to be found
     * @return The wallet or an WalletNotFoundException
     */
    Mono<WalletDto> findWallet(Long walletId);
}
