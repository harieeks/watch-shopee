package com.ecommerce.library.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponDto {

    private String couponName;
    private String couponCode;
    private String expiry;
    private Double offerPrice;
}
