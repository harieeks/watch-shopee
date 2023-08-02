package com.ecommerce.library.service.impl;

import com.ecommerce.library.Dto.CustomerDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.repository.CustomerRepository;
import com.ecommerce.library.repository.RoleRepository;
import com.ecommerce.library.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private  CustomerRepository customerRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Override
    public Customer findByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    @Override
    public boolean existByPhoneNo(String phoneNo) {
        return customerRepository.existsByPhoneNumber(phoneNo);
    }

    @Override
    public Customer save(CustomerDto customerDto) {
        Customer customer=new Customer();
        customer.setName(customerDto.getName());
        customer.setUsername(customerDto.getUsername());
        customer.setPassword(customerDto.getPassword());
        customer.setPhoneNumber(customerDto.getPhoneNo());
        customer.setEnabled(false);
        customer.setDeleted(false);
        customer.setRoles(Arrays.asList(roleRepository.findByName("CUSTOMER")));
        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> findAllCustomer() {
        return customerRepository.findAll();
    }

    @Override
    public void blockUser(Long id) {
        Customer customer=customerRepository.getById(id);
        customer.setDeleted(true);
        customer.setEnabled(false);
        customerRepository.save(customer);
    }

    @Override
    public void unblockUser(Long id) {
        Customer customer=customerRepository.getById(id);
        customer.setDeleted(false);
        customer.setEnabled(true);
        customerRepository.save(customer);
    }

    @Override
    public Page<Customer> pageCustomer(int pageNo,String sortBy) {
        Pageable pageable= PageRequest.of(pageNo,5, Sort.by(sortBy));
        Page<Customer> customerPage=customerRepository.pageCustomer(pageable);
        return customerPage;
    }

    @Override
    public Page<Customer> searchCustomer(int pageNo, String keyword) {
        Pageable pageable=PageRequest.of(pageNo,5);
        Page<Customer> customerPage=customerRepository.searchCustomer(keyword,pageable);
        return customerPage;
    }

    @Override
    public Customer findById(Long id) {
       return customerRepository.getById(id);
    }

    @Override
    public void enableAfterOtp(Customer customer) {
        customerRepository.save(customer);
    }
}
