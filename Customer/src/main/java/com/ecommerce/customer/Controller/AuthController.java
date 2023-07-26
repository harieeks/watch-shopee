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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
        model.addAttribute("twilioDto",new TwilioDto());
        return "register";
    }



    @PostMapping("/register-new")
    public String newRegister(@Valid @ModelAttribute("customerDto")CustomerDto customerDto,
                              BindingResult result,
                              Model model
                              ){
        try {
            if(result.hasErrors()){
                model.addAttribute("customerDto",customerDto);
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
               // TwilioDto twilioDto=new TwilioDto();





                System.out.println("Customer provided username"+customerDto.getPhoneNo());
               // System.out.println("NUmber set in twiliodto"+twilioDto.getPhoneNO());
                model.addAttribute("customerId",customerNew.getId());
                model.addAttribute("twilio",new TwilioDto());
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

    @PostMapping("/otp-verification/{id}")
    public String otpVerification(@ModelAttribute("twilio")TwilioDto twilioDto,
                                  @PathVariable("id")Long id,
                                  RedirectAttributes attributes){

        try {
            Customer customer=customerService.findById(id);
            Otp otp=otpService.findByNumber(customer.getPhoneNumber());
            System.out.println("This is my phone number"+customer.getPhoneNumber());
            System.out.println("This is your otp from database"+otp.getOtp());
            System.out.println("User Entered otp"+twilioDto.getOtp());

            if(otp.getOtp().equals(twilioDto.getOtp())){
                attributes.addFlashAttribute("success","Otp Verified Success fully");
                //otpService.deleteOtp(otp.getId());
                //customerService.save(customerDto);
                System.out.println("otp verified successfully");
                return "login";
            }else {
                attributes.addFlashAttribute("error","Otp you provided is in correct");
                return "redirect:/otpLogin";
            }
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error","Something went wrong ");
        }
        return "otpLogin";
    }
}
