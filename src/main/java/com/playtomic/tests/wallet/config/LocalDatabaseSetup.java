package com.playtomic.tests.wallet.config;

import com.playtomic.tests.wallet.dao.WalletDao;
import com.playtomic.tests.wallet.repository.WalletRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Configuration
public class LocalDatabaseSetup {

    @Bean
    ApplicationRunner init(WalletRepository repository, DatabaseClient client) {
        return args -> createTableIfNotExists(client)
                .then(emptyTable(client))
                .thenMany(createWallets(repository))
                .subscribe();
    }

    private Mono<Map<String, Object>> createTableIfNotExists(DatabaseClient client) {
        return client.sql("create table IF NOT EXISTS wallet" +
                "(id SERIAL PRIMARY KEY, current_balance DOUBLE);").fetch().first();
    }

    private Mono<Map<String, Object>> emptyTable(DatabaseClient client) {
        return client.sql("DELETE FROM wallet;").fetch().first();
    }

    private Flux<WalletDao> createWallets(WalletRepository repository) {
        return repository.saveAll(Flux.fromIterable(List.of(
                createWallet(BigDecimal.valueOf(1000)),
                createWallet(BigDecimal.valueOf(500.3)),
                createWallet(BigDecimal.valueOf(800.3)),
                createWallet(BigDecimal.valueOf(2500.56)))));
    }

    private WalletDao createWallet(BigDecimal currentBalance) {
        return new WalletDao(null, currentBalance);
    }
}
