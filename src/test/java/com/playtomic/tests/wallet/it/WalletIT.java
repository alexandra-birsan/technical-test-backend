package com.playtomic.tests.wallet.it;

import com.playtomic.tests.wallet.api.WalletController;
import com.playtomic.tests.wallet.exception.CustomExceptionsHandler;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.utils.ITUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
public class WalletIT {

    @Autowired
     WalletController walletController;
    @Autowired
     WalletRepository walletRepository;
    @Autowired
     CustomExceptionsHandler customExceptionsHandler;

     ITUtils testUtils;

    @BeforeEach
    public void init() {
        WebTestClient webClient = WebTestClient.bindToController(walletController)
                .controllerAdvice(customExceptionsHandler)
                .build();
        testUtils = new ITUtils(webClient);
    }

}
