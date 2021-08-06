package com.playtomic.tests.wallet.repository;


import com.playtomic.tests.wallet.dao.WalletDao;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends ReactiveCrudRepository<WalletDao, Long> {
}
