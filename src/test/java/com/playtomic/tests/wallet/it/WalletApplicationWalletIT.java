package com.playtomic.tests.wallet.it;

import com.playtomic.tests.wallet.dao.WalletDao;
import com.playtomic.tests.wallet.dto.PaymentRequest;
import com.playtomic.tests.wallet.dto.TopUpRequest;
import com.playtomic.tests.wallet.dto.WalletDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static com.playtomic.tests.wallet.config.LocalDatabaseSetup.AMOUNT_BY_WALLET_ID;
import static com.playtomic.tests.wallet.utils.TestDataUtils.*;

public class WalletApplicationWalletIT extends WalletIT {

    @Test
    void findById() {
        long walletId = 1L;

        testUtils.sendFindWalletRequest(walletId)
                .expectStatus().isOk()
                .expectBody(WalletDto.class)
                .isEqualTo(createWalletDto(walletId, AMOUNT_BY_WALLET_ID.get(walletId)));
    }

    @Test
    void recharge() {
        long walletId = 2L;

        testUtils.sendTopUpRequest(walletId, createTopUpRequest())
                .expectStatus().isOk()
                .expectBody(WalletDto.class)
                .isEqualTo(createWalletDto(walletId, AMOUNT_BY_WALLET_ID.get(walletId).add(TOP_UP_AMOUNT)));
    }

    @Test
    void walletNotRechargedWhenStripeExceptionThrown() {
        long walletId = 3L;
        TopUpRequest topUpRequest = createTopUpRequest();
        topUpRequest.setAmount(BigDecimal.valueOf(4));

        testUtils.sendTopUpRequest(walletId, topUpRequest)
                .expectStatus()
                .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);


        StepVerifier.create(walletRepository.findById(walletId))
                .expectNext(new WalletDao(walletId, AMOUNT_BY_WALLET_ID.get(walletId)))
                .verifyComplete();
    }

    @Test
    void charge() {
        long walletId = 4L;

        testUtils.sendPaymentRequest(walletId, createPaymentRequest())
                .expectStatus().isOk()
                .expectBody(WalletDto.class)
                .isEqualTo(createWalletDto(walletId, AMOUNT_BY_WALLET_ID.get(walletId).subtract(PAYMENT_AMOUNT)));
    }

    private WalletDto createWalletDto(long walletId, BigDecimal bigDecimal) {
        return new WalletDto(walletId, bigDecimal);
    }

    @Test
    void chargeWhenIncorrectRequestBody() {
        long walletId = 4L;

        testUtils.sendPaymentRequest(walletId, new PaymentRequest()).expectStatus().isBadRequest();
    }

    @Test
    void findByIdWhenWalletNotFound() {
        testUtils.sendFindWalletRequest(100).expectStatus().isNotFound();
    }
}
