package com.ecommerce.library.repository;

import com.ecommerce.library.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Long> {

    Wallet findByCustomerId(Long id);
}
