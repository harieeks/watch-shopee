package com.ecommerce.library.service.impl;

import com.ecommerce.library.Dto.CouponDto;
import com.ecommerce.library.model.Coupon;
import com.ecommerce.library.repository.CouponRepository;
import com.ecommerce.library.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Override
    public Coupon createCoupon(CouponDto couponDto, Date expiryDate) {
        Coupon coupon=new Coupon();
        coupon.setCouponName(couponDto.getCouponName());
        coupon.setCouponCode(couponDto.getCouponCode());
        coupon.setCouponDiscount(couponDto.getOfferPrice());
        coupon.setExpiry(expiryDate);
        coupon.setIsActive(true);

        return couponRepository.save(coupon);
    }

    @Override
    public List<Coupon> findAllCoupon() {
        return couponRepository.findAll();
    }

    @Override
    public void deActivateCoupon(Long id) {
        Coupon coupon=couponRepository.findCouponById(id);
        coupon.setIsActive(false);
        couponRepository.save(coupon);
    }

    @Override
    public void activateCoupon(Long id) {
        Coupon coupon=couponRepository.findCouponById(id);
        coupon.setIsActive(true);
        couponRepository.save(coupon);
    }

    @Override
    public Coupon findById(Long id) {
        return couponRepository.findCouponById(id);
    }

    @Override
    public Coupon editCoupon(Coupon coupon,Date expiry) {

        Coupon oldCoupon=couponRepository.findCouponById(coupon.getId());
        oldCoupon.setCouponName(coupon.getCouponName());
        oldCoupon.setCouponCode(coupon.getCouponCode());
        oldCoupon.setCouponDiscount(coupon.getCouponDiscount());
        oldCoupon.setIsActive(true);
        oldCoupon.setExpiry(expiry);
        return couponRepository.save(oldCoupon);
    }

    @Override
    public Coupon findCouponByCouponCode(String code) {
        return couponRepository.findCouponByCouponCode(code);
    }
}
