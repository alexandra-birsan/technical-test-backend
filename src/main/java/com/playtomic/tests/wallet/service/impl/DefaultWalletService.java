package com.playtomic.tests.wallet.service.impl;

import com.playtomic.tests.wallet.api.WalletController;
import com.playtomic.tests.wallet.dao.WalletDao;
import com.playtomic.tests.wallet.dto.ChargeRequest;
import com.playtomic.tests.wallet.dto.RechargeRequest;
import com.playtomic.tests.wallet.dto.WalletDto;
import com.playtomic.tests.wallet.exception.InsufficientFundsException;
import com.playtomic.tests.wallet.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.function.Function;

@Service
public class DefaultWalletService implements WalletService {

    private static final Logger log = LoggerFactory.getLogger(WalletController.class);

    private final DefaultStripeService stripeService;
    private final WalletRepository walletRepository;

    public DefaultWalletService(DefaultStripeService stripeService, WalletRepository walletRepository) {
        this.stripeService = stripeService;
        this.walletRepository = walletRepository;
    }

    @Override
    public Mono<WalletDto> charge(Long walletId, ChargeRequest request) {
        return findWalletDao(walletId)
                .filter(walletDao -> walletDao.getCurrentBalance().compareTo(request.getAmount()) >= 0)
                .switchIfEmpty(Mono.error(new InsufficientFundsException()))
                .flatMap(walletDao -> {
                    BigDecimal updatedBalance = walletDao.getCurrentBalance().subtract(request.getAmount());
                    return walletRepository.save(new WalletDao(walletId, updatedBalance));
                })
                .map(fromDaoToDto())
                .doOnError(throwable -> log.error("Error while charging the wallet {}: {}", walletId, throwable.getMessage()))
                .doOnNext(walletDto -> log.debug("Successfully charged the wallet {}", walletId));
    }

    @Override
    public Mono<WalletDto> recharge(Long walletId, RechargeRequest request) {
        return findWalletDao(walletId)
                .flatMap(wallet -> stripeService.charge(request.getCreditCardNumber(), request.getAmount())
                        .map(v -> wallet))
                .flatMap(walletDao -> {
                    BigDecimal updatedBalance = walletDao.getCurrentBalance().add(request.getAmount());
                    return walletRepository.save(new WalletDao(walletId, updatedBalance));
                })
                .map(fromDaoToDto())
                .doOnError(throwable -> log.error("Error while recharging the wallet {}: {}", walletId, throwable.getMessage()))
                .doOnNext(walletDto -> log.debug("Successfully recharged the wallet {}", walletId));
    }

    @Override
    public Mono<WalletDto> findWallet(Long walletId) {
        return walletRepository.findById(walletId)
                .switchIfEmpty(Mono.error(new WalletNotFoundException()))
                .map(fromDaoToDto());
    }

    private Mono<WalletDao> findWalletDao(Long walletId) {
        return walletRepository.findById(walletId)
                .switchIfEmpty(Mono.error(new WalletNotFoundException()));
    }

    private Function<WalletDao, WalletDto> fromDaoToDto() {
        return walletDao -> new WalletDto(walletDao.getId(), walletDao.getCurrentBalance());
    }
}
