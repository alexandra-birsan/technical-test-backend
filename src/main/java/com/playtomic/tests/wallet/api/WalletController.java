package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.dto.ChargeRequest;
import com.playtomic.tests.wallet.dto.RechargeRequest;
import com.playtomic.tests.wallet.dto.WalletDto;
import com.playtomic.tests.wallet.service.impl.DefaultWalletService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/wallet/{id}")
@Validated
public class WalletController {

    private final DefaultWalletService walletService;

    public WalletController(DefaultWalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping
    public Mono<WalletDto> getWalletById(@PathVariable Long id) {
        return walletService.findWallet(id);
    }

    @PutMapping(value = "/top-up", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<WalletDto> rechargeWallet(@PathVariable Long id, @Valid @RequestBody RechargeRequest request) {
        return walletService.recharge(id, request);
    }

    @PutMapping(value = "/payment", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<WalletDto> chargeWallet(@PathVariable Long id,
                                        @Valid @RequestBody ChargeRequest request) {
        return walletService.charge(id, request);
    }
}
