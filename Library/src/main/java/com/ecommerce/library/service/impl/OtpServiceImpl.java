package com.ecommerce.library.service.impl;


import com.ecommerce.library.Dto.TwilioDto;
import com.ecommerce.library.model.Otp;
import com.ecommerce.library.repository.OtpRepository;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.internal.LoadingCache;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class OtpServiceImpl {



    @Autowired
    private OtpRepository otpRepository;
    private static final Integer EXPIRE_MIN=5;

    public String generateOtp(String phoneNo){
        PhoneNumber to=new PhoneNumber("+919207692551");
        PhoneNumber from=new PhoneNumber("+12566701431");
        String otp=getRandomOtp(phoneNo);
        String otpMsg="Dear customer this is your otp for COZASTORE login "+otp+" use this";
        Message
                .creator(to,from,otpMsg)
                .create();
        return otp;
    }

    private String getRandomOtp(String phoneNo) {
        String otp =  new DecimalFormat("000000")
                .format(new Random().nextInt(999999));
        return otp;
    }

    public Otp save(Otp otp){
        return otpRepository.save(otp);
    }

    public void deleteOtp(Long id){
        otpRepository.deleteById(id);
    }

    public Otp findByNumber(String phoneNo){
        return otpRepository.findByPhoneNo(phoneNo);
    }




}
