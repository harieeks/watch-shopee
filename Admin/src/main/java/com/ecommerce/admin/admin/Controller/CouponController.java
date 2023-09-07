package com.ecommerce.admin.admin.Controller;

import com.ecommerce.library.Dto.CouponDto;
import com.ecommerce.library.model.Coupon;
import com.ecommerce.library.service.CouponService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class CouponController {

    @Autowired
    private CouponService couponService;

    @GetMapping("/coupon")
    public String getCoupon(Model model){
        model.addAttribute("coupon",new CouponDto());
        List<Coupon> coupons=couponService.findAllCoupon();
        model.addAttribute("couponList",coupons);
        return "coupon";
    }

    @PostMapping("/create-coupon")
    public String createCoupon(@ModelAttribute("coupon") CouponDto couponDto) {
        String expiry = couponDto.getExpiry();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date expiryDate = null;
        try {
            expiryDate = dateFormat.parse(expiry);
        } catch (Exception e) {
            e.printStackTrace();
        }
        couponService.createCoupon(couponDto, expiryDate);
        return "redirect:/coupon";
    }

    @RequestMapping(value ="/deActivate-coupon/{id}",method = {RequestMethod.POST,RequestMethod.GET})
    public String deActivateCoupon(@PathVariable("id") Long id,RedirectAttributes attributes){
        try {
            couponService.deActivateCoupon(id);
            attributes.addFlashAttribute("success"," Coupon De-Activated");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error","Failed to de-Activate");
        }
        return "redirect:/coupon";
    }

    @RequestMapping(value ="/activate-coupon/{id}",method = {RequestMethod.POST,RequestMethod.GET})
    public String activateCoupon(@PathVariable("id") Long id, RedirectAttributes attributes){
        try {
            couponService.activateCoupon(id);
            attributes.addFlashAttribute("success","Coupon Activated ");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error","Failed to Activate");
        }
        return "redirect:/coupon";
    }

    @RequestMapping(value = "/findCouponById", method = {RequestMethod.PUT,RequestMethod.GET})
    @ResponseBody
    public Coupon findCouponById(Long id){
         return couponService.findById(id);
    }

    @GetMapping("/edit-coupon")
    public String editCoupon(Coupon coupon,RedirectAttributes attributes){
        String expiry=String.valueOf(coupon.getExpiry());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date expiryDate = null;
        try {
            expiryDate = dateFormat.parse(expiry);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            couponService.editCoupon(coupon,expiryDate);
            attributes.addFlashAttribute("success","Coupon edited successfully");

        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error","Filed to edit");
        }
        return "redirect:/coupon";
    }
}
