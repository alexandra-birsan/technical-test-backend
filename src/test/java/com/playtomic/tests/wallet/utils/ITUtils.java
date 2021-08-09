package com.playtomic.tests.wallet.utils;

import com.playtomic.tests.wallet.dto.PaymentRequest;
import com.playtomic.tests.wallet.dto.TopUpRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

public class ITUtils {

    private final WebTestClient webClient;

    public ITUtils(WebTestClient webClient) {
        this.webClient = webClient;
    }

    public WebTestClient.ResponseSpec sendTopUpRequest(long walletId, TopUpRequest requestBody) {
        return sendPutRequest(walletId, "/wallet/%s/top-up", requestBody);
    }

    public WebTestClient.ResponseSpec sendPaymentRequest(long walletId,
                                                         PaymentRequest requestBody) {
        return sendPutRequest(walletId, "/wallet/%s/payment", requestBody);
    }

    private WebTestClient.ResponseSpec sendPutRequest(long walletId, String uri, Object requestBody) {
        return webClient.put()
                .uri(String.format(uri, walletId))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(requestBody)
                .exchange();
    }

    public WebTestClient.ResponseSpec sendFindWalletRequest(long walletId) {
        return webClient.get()
                .uri(String.format("/wallet/%s", walletId))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange();
    }
}
