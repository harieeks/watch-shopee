package com.ecommerce.library.service.impl;

import com.ecommerce.library.model.Wallet;
import com.ecommerce.library.repository.WalletRepository;
import com.ecommerce.library.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;
    @Override
    public Wallet findCustomerWallet(Long id) {
        return walletRepository.findByCustomerId(id);
    }
}
