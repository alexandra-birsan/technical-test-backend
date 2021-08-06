package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.dto.ChargeRequest;
import com.playtomic.tests.wallet.dto.WalletDto;
import com.playtomic.tests.wallet.exception.AppError;
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

import static com.playtomic.tests.wallet.utils.TestData.*;
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
        ChargeRequest chargeRequest = new ChargeRequest();
        chargeRequest.setAmount(Optional.ofNullable(rechargeAmount).map(BigDecimal::valueOf).orElse(null));

        sendPaymentRequest(chargeRequest)
                .expectStatus().isBadRequest()
                .expectBody(AppError.class)
                .isEqualTo(new AppError("Invalid amount!"));
    }

    @Test
    void successfulCharge() {
        ChargeRequest chargeRequest = createChargeRequest();
        WalletDto data = new WalletDto(WALLET_ID, BigDecimal.valueOf(CHARGE_AMOUNT));
        when(walletService.charge(WALLET_ID, chargeRequest)).thenReturn(Mono.just(data));

        sendPaymentRequest(chargeRequest)
                .expectStatus().isOk()
                .expectBody(WalletDto.class)
                .isEqualTo(data);
    }

    private WebTestClient.ResponseSpec sendPaymentRequest(ChargeRequest chargeRequest) {
        return webClient.put()
                .uri(String.format("/wallet/%s/payment", WALLET_ID))
                .bodyValue(chargeRequest)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange();
    }
}

