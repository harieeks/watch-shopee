package com.ecommerce.library.repository;

import com.ecommerce.library.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends JpaRepository<Otp,Long> {

    Otp findByPhoneNo(String phoneNO);
}
