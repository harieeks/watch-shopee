package com.ecommerce.library.service;

import com.ecommerce.library.model.Wallet;
import com.ecommerce.library.repository.WalletRepository;

public interface WalletService {

    Wallet findCustomerWallet(Long id);
}
