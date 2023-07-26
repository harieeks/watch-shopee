package com.ecommerce.library.service;

import com.ecommerce.library.Dto.CustomerDto;
import com.ecommerce.library.model.Customer;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerService {

    Customer findByUsername(String username);

    Customer save(CustomerDto customerDto);

    List<Customer> findAllCustomer();

    void blockUser(Long id);

    void unblockUser(Long id);
    Page<Customer> pageCustomer(int pageNo,String sortBy);

    Page<Customer> searchCustomer(int pageNo,String keyword);

    Customer findById(Long id);
}
