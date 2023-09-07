package com.ecommerce.library.service;

import com.ecommerce.library.Dto.CouponDto;
import com.ecommerce.library.model.Coupon;

import java.util.Date;
import java.util.List;

public interface CouponService {

    Coupon createCoupon(CouponDto couponDto, Date expiryDate);
    List<Coupon> findAllCoupon();
    void deActivateCoupon(Long id);
    void activateCoupon(Long id);
    Coupon findById(Long id);
    Coupon editCoupon(Coupon coupon,Date expiry);
    Coupon findCouponByCouponCode(String code);
}
