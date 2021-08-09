package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.dto.PaymentRequest;
import com.playtomic.tests.wallet.dto.WalletDto;
import com.playtomic.tests.wallet.service.impl.DefaultWalletService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Optional;

import static com.playtomic.tests.wallet.utils.TestDataUtils.*;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = WalletController.class)
public class WalletControllerTest {

    @Autowired
    private WebTestClient webClient;
    @MockBean
    private DefaultWalletService walletService;

    @ParameterizedTest
    @ValueSource(doubles = {-2.0, 0.0})
    @NullSource
    void invalidChargeAmount(Double rechargeAmount) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(Optional.ofNullable(rechargeAmount).map(BigDecimal::valueOf).orElse(null));

        sendPaymentRequest(paymentRequest)
                .expectStatus().isBadRequest();
    }

    @Test
    void successfulCharge() {
        PaymentRequest paymentRequest = createPaymentRequest();
        WalletDto data = new WalletDto(WALLET_ID, PAYMENT_AMOUNT);
        when(walletService.charge(WALLET_ID, paymentRequest)).thenReturn(Mono.just(data));

        sendPaymentRequest(paymentRequest)
                .expectStatus().isOk()
                .expectBody(WalletDto.class)
                .isEqualTo(data);
    }

    private WebTestClient.ResponseSpec sendPaymentRequest(PaymentRequest paymentRequest) {
        return webClient.put()
                .uri(String.format("/wallet/%s/payment", WALLET_ID))
                .bodyValue(paymentRequest)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange();
    }
}

