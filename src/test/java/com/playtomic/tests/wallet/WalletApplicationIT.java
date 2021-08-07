package com.playtomic.tests.wallet;

import com.playtomic.tests.wallet.api.WalletController;
import com.playtomic.tests.wallet.dto.PaymentRequest;
import com.playtomic.tests.wallet.dto.WalletDto;
import com.playtomic.tests.wallet.exception.CustomExceptionsHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

import static com.playtomic.tests.wallet.utils.TestData.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
public class WalletApplicationIT {

    @Autowired
    private WalletController walletController;
    @Autowired
    private CustomExceptionsHandler customExceptionsHandler;

    private WebTestClient webClient;

    @BeforeEach
    public void init() {
        webClient = WebTestClient.bindToController(walletController)
                .controllerAdvice(customExceptionsHandler)
                .build();
    }

    // The expected balance of a wallet uses the data from the wallet creation, present in LocalDatabaseSetup

    @Test
    void findById() {
        long walletId = 1L;

        webClient.get()
                .uri(String.format("/wallet/%s", walletId))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk()
                .expectBody(WalletDto.class)
                .isEqualTo(createWalletDto(walletId, BigDecimal.valueOf(1000.0)));
    }

    @Test
    void recharge() {
        long walletId = 2L;

        sendPutRequest(walletId, "/wallet/%s/top-up")
                .bodyValue(createTopUpRequest())
                .exchange()
                .expectStatus().isOk()
                .expectBody(WalletDto.class)
                .isEqualTo(createWalletDto(walletId, BigDecimal.valueOf(500.3).add(TOP_UP_AMOUNT)));
    }

    @Test
    void charge() {
        long walletId = 3L;

        sendPutRequest(walletId, "/wallet/%s/payment")
                .bodyValue(createPaymentRequest())
                .exchange()
                .expectStatus().isOk()
                .expectBody(WalletDto.class)
                .isEqualTo(createWalletDto(walletId, BigDecimal.valueOf(2500.56).subtract(PAYMENT_AMOUNT)));
    }

    private WalletDto createWalletDto(long walletId, BigDecimal bigDecimal) {
        return new WalletDto(walletId, bigDecimal);
    }

    @Test
    void chargeWhenIncorrectRequestBody() {
        long walletId = 3L;

        sendPutRequest(walletId, "/wallet/%s/payment")
                .bodyValue(new PaymentRequest())
                .exchange()
                .expectStatus().isBadRequest();
    }

    private WebTestClient.RequestBodySpec sendPutRequest(long walletId, String uri) {
        return webClient.put()
                .uri(String.format(uri, walletId))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    void findByIdWhenWalletNotFound() {
        webClient.get()
                .uri(String.format("/wallet/%s", 100))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isNotFound();
    }
}
