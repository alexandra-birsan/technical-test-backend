package com.playtomic.tests.wallet.service.impl;

import com.playtomic.tests.wallet.dao.WalletDao;
import com.playtomic.tests.wallet.dto.RechargeRequest;
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
        RechargeRequest request = createRechargeRequest();

        sendTopUpRequest(request)
                .expectError(WalletNotFoundException.class)
                .verify();
    }

    @Test
    void rechargeWalletWhenStripeExceptionThrown() {
        mockSuccessfulFindById();
        when(stripeService.charge(CREDIT_CARD_NUMBER, RECHARGE_AMOUNT)).thenReturn(Mono.error(new StripeServiceException()));
        RechargeRequest request = createRechargeRequest();

        sendTopUpRequest(request)
                .expectError(StripeServiceException.class)
                .verify();
    }

    @Test
    void rechargeWalletSuccessfully() {
        mockSuccessfulFindById();
        mockSuccessfulSave();
        mockSuccessfulStripeCall();
        RechargeRequest request = createRechargeRequest();

        sendTopUpRequest(request)
                .expectNext(new WalletDto(WALLET_ID, CURRENT_BALANCE.add(RECHARGE_AMOUNT)))
                .verifyComplete();
    }

    private StepVerifier.FirstStep<WalletDto> sendTopUpRequest(RechargeRequest request) {
        return StepVerifier.create(walletService.recharge(WALLET_ID, request));
    }

    private void mockSuccessfulFindById() {
        when(repository.findById(WALLET_ID)).thenReturn(Mono.just(new WalletDao(WALLET_ID, CURRENT_BALANCE)));
    }

    private void mockSuccessfulSave() {
        WalletDao updatedWallet = new WalletDao(WALLET_ID, CURRENT_BALANCE.add(RECHARGE_AMOUNT));
        when(repository.save(updatedWallet)).thenReturn(Mono.just(updatedWallet));
    }

    private void mockSuccessfulStripeCall() {
        when(stripeService.charge(CREDIT_CARD_NUMBER, RECHARGE_AMOUNT)).thenReturn(Mono.just(true));
    }

    private RechargeRequest createRechargeRequest() {
        RechargeRequest request = new RechargeRequest();
        request.setAmount(RECHARGE_AMOUNT);
        request.setCreditCardNumber(CREDIT_CARD_NUMBER);
        return request;
    }
}
