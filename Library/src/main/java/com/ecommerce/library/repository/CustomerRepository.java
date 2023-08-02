package com.ecommerce.library.repository;

import com.ecommerce.library.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {

    Customer findByUsername(String username);
    boolean existsByPhoneNumber(String phoneNo);
    @Query("select c from Customer c")
    Page<Customer> pageCustomer(Pageable pageable);

    @Query("select c from  Customer c where c.name like %?1%")
    Page<Customer> searchCustomer(String keyword,Pageable pageable);
}
