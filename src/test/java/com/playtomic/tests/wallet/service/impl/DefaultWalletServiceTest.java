package com.playtomic.tests.wallet.service.impl;

import com.playtomic.tests.wallet.dao.WalletDao;
import com.playtomic.tests.wallet.dto.TopUpRequest;
import com.playtomic.tests.wallet.dto.WalletDto;
import com.playtomic.tests.wallet.exception.StripeServiceException;
import com.playtomic.tests.wallet.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static com.playtomic.tests.wallet.utils.TestData.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DefaultWalletServiceTest {

    @Mock
    private WalletRepository repository;
    @Mock
    private DefaultStripeService stripeService;
    @InjectMocks
    private DefaultWalletService walletService;

    @Test
    void findWalletWhenNotFound() {
        when(repository.findById(WALLET_ID)).thenReturn(Mono.empty());

        StepVerifier.create(walletService.findWallet(WALLET_ID))
                .expectError(WalletNotFoundException.class)
                .verify();
    }

    @Test
    void findWalletSuccessfully() {
        mockSuccessfulFindById();

        StepVerifier.create(walletService.findWallet(WALLET_ID))
                .expectNext(new WalletDto(WALLET_ID, CURRENT_BALANCE))
                .verifyComplete();
    }

    @Test
    void rechargeWalletWhenWalletNotFound() {
        when(repository.findById(WALLET_ID)).thenReturn(Mono.error(new WalletNotFoundException()));

        sendTopUpRequest(createTopUpRequest())
                .expectError(WalletNotFoundException.class)
                .verify();
    }

    @Test
    void rechargeWalletWhenStripeExceptionThrown() {
        mockSuccessfulFindById();
        when(stripeService.charge(CREDIT_CARD_NUMBER, TOP_UP_AMOUNT)).thenReturn(Mono.error(new StripeServiceException()));
        mockSuccessfulSave(CURRENT_BALANCE);
        mockSuccessfulSave(CURRENT_BALANCE.add(TOP_UP_AMOUNT));

        sendTopUpRequest(createTopUpRequest())
                .expectError(StripeServiceException.class)
                .verify();
    }

    @Test
    void rechargeWalletSuccessfully() {
        mockSuccessfulFindById();
        mockSuccessfulSave(CURRENT_BALANCE.add(TOP_UP_AMOUNT));
        mockSuccessfulStripeCall();

        sendTopUpRequest(createTopUpRequest())
                .expectNext(new WalletDto(WALLET_ID, CURRENT_BALANCE.add(TOP_UP_AMOUNT)))
                .verifyComplete();
    }

    private StepVerifier.FirstStep<WalletDto> sendTopUpRequest(TopUpRequest request) {
        return StepVerifier.create(walletService.recharge(WALLET_ID, request));
    }

    private void mockSuccessfulFindById() {
        when(repository.findById(WALLET_ID)).thenReturn(Mono.just(new WalletDao(WALLET_ID, CURRENT_BALANCE)));
    }

    private void mockSuccessfulStripeCall() {
        when(stripeService.charge(CREDIT_CARD_NUMBER, TOP_UP_AMOUNT)).thenReturn(Mono.just(true));
    }

    private void mockSuccessfulSave(BigDecimal currentBalance) {
        WalletDao updatedWallet = new WalletDao(WALLET_ID, currentBalance);
        when(repository.save(updatedWallet)).thenReturn(Mono.just(updatedWallet));
    }
}
