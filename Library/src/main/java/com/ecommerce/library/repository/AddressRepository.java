package com.ecommerce.library.repository;

import com.ecommerce.library.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {

    @Query(value = "SELECT * FROM address WHERE customer_id = :customerId ORDER BY random() LIMIT 1", nativeQuery = true)
    Address findRandomAddressByCustomerId(Long customerId);
}
