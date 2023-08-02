package com.ecommerce.customer.Controller;

import com.ecommerce.library.Dto.CustomerDto;
import com.ecommerce.library.Dto.TwilioDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Otp;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.impl.OtpServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private OtpServiceImpl otpService;
    @Autowired
    private  BCryptPasswordEncoder passwordEncoder;


    @GetMapping("/login")
    public String doLogin(){
        return "login";
    }

    @GetMapping("/register")
    public String getRegister(Model model){
        model.addAttribute("customerDto",new CustomerDto());
       // model.addAttribute("twilioDto",new TwilioDto());
        return "register";
    }

    @GetMapping("/otpVerification")
    public String otpVerification(){
        return "otpLogin";
    }

    @PostMapping("/register-new")
    public String newRegister(@Valid @ModelAttribute("customerDto")CustomerDto customerDto,
                              BindingResult result,
                              Model model
                              ){
        try {
            if(result.hasErrors()){
                model.addAttribute("customerDto",customerDto);
                model.addAttribute("error","Something Went wrong please try again");
                System.out.println("HAS ERROR IN RESULT");
                return "register";
            }
            Customer customer=customerService.findByUsername(customerDto.getUsername());
            if(customer!=null){
                model.addAttribute("customerDto",customerDto);
                model.addAttribute("emailError","This email is all ready used");
                System.out.println("HAS ERROR IN EMAIL");
                return "register";
            }

            if(customerService.existByPhoneNo(customerDto.getPhoneNo())){
                model.addAttribute("customerDto",customerDto);
                model.addAttribute("emailError","This Phone Number is all ready used");
                return "register";
            }

            if(customerDto.getPassword().equals(customerDto.getRepeatPassword())){
                customerDto.setPassword(passwordEncoder.encode(customerDto.getPassword()));
               Customer customerNew= customerService.save(customerDto);
                model.addAttribute("success","Registered success fully");
                System.out.println("NO ERROR EVERY THING OK");

                String otp= otpService.generateOtp(customerDto.getPhoneNo());
                Otp otpNew=new Otp();
                otpNew.setOtp(otp);
                otpNew.setPhoneNo(customerDto.getPhoneNo());
                otpService.save(otpNew);
                System.out.println("OTP NUMBER"+otp);

                model.addAttribute("twilio",new TwilioDto());
                Long customerId=customerNew.getId();
                model.addAttribute("id",customerId);
                model.addAttribute("phoneNumber",customerDto.getPhoneNo());
                return "otpLogin";

            }else{
                model.addAttribute("error","Password is incorrect");
                System.out.println("password is incorrect");
                return "register";
            }
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("error","Something went wrong please try again");
            System.out.println("FULL OF ERRORS");
        }
        return "register";
    }

    @PostMapping ("/otp-doVerification")
    public String otpVerification(@RequestParam("id") Long id,
                                  @ModelAttribute("twilio")TwilioDto twilioDto,
                                  RedirectAttributes attributes){

        try {

            System.out.println("Customer id"+id);
            Customer customer=customerService.findById(id);
            System.out.println("Customer from db"+customer);
            String phNO= customer.getPhoneNumber();
            Otp otp= otpService.findByNumber(phNO);

            if(otp.getOtp().equals(twilioDto.getOtp())){

                customer.setEnabled(true);
                customerService.enableAfterOtp(customer);
                return "login";
            }
            else{
                attributes.addFlashAttribute("errorotp","Incorrect otp ");
                return "otpLogin";
            }
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("errorotp","Something went wrong");
            return "register";
        }
    }


}
