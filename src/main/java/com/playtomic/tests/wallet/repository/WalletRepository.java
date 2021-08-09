package com.playtomic.tests.wallet.repository;


import com.playtomic.tests.wallet.dao.WalletDao;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface WalletRepository extends ReactiveCrudRepository<WalletDao, Long> {

    @Query("SELECT * FROM wallet WHERE id = ?1 FOR UPDATE")
    Mono<WalletDao> findById(Long id);
}
