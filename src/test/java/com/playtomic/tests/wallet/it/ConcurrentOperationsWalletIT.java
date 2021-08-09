package com.playtomic.tests.wallet.it;

import com.playtomic.tests.wallet.dao.WalletDao;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.math.BigDecimal;

import static com.playtomic.tests.wallet.config.LocalDatabaseSetup.AMOUNT_BY_WALLET_ID;
import static com.playtomic.tests.wallet.utils.TestDataUtils.*;

public class ConcurrentOperationsWalletIT extends WalletIT {

    @Test
    void checkConcurrentPaymentAndTopUp() {
        long walletId = 5L;
        var topUpAndPayment = createTopUpAndPaymentRequests(walletId);
        Mono<WalletDao> updatedValueInDB = topUpAndPayment
                .flatMap(walletDao -> walletRepository.findById(walletId));
        BigDecimal expectedAmount = AMOUNT_BY_WALLET_ID.get(walletId).add(TOP_UP_AMOUNT).subtract(PAYMENT_AMOUNT);

        StepVerifier.create(updatedValueInDB)
                .expectNext(new WalletDao(walletId, expectedAmount))
                .verifyComplete();
    }

    private Mono<Tuple2<WebTestClient.ResponseSpec, WebTestClient.ResponseSpec>> createTopUpAndPaymentRequests(long walletId) {
        var topUpRequest = getTopUpRequest(walletId);
        var paymentRequest = getPaymentRequest(walletId);

        return topUpRequest.zipWith(paymentRequest);
    }

    private Mono<WebTestClient.ResponseSpec> getPaymentRequest(long walletId) {
        return Mono.just(testUtils.sendPaymentRequest(walletId, createPaymentRequest()))
                .subscribeOn(Schedulers.parallel())
                .doOnNext(responseSpec -> responseSpec.expectStatus().isOk());
    }

    private Mono<WebTestClient.ResponseSpec> getTopUpRequest(long walletId) {
        return Mono.just(testUtils.sendTopUpRequest(walletId, createTopUpRequest()))
                .subscribeOn(Schedulers.parallel())
                .doOnNext(responseSpec -> responseSpec.expectStatus().isOk());
    }
}
