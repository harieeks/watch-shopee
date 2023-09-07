package com.ecommerce.library.repository;

import com.ecommerce.library.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon,Long> {

    Coupon findCouponById(Long id);
    Coupon findCouponByCouponCode(String code);
}
