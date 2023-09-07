package com.ecommerce.customer.Controller;

import com.ecommerce.library.Dto.CustomerDto;
import com.ecommerce.library.Dto.TwilioDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Otp;
import com.ecommerce.library.repository.CustomerRepository;
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
    @Autowired
    private CustomerRepository customerRepository;


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
        System.out.println("registration");
        try {
//            if(result.hasErrors()){
//                model.addAttribute("customerDto",customerDto);
//                model.addAttribute("error","Something Went wrong please try again");
//                System.out.println("HAS ERROR IN RESULT");
//                return "register";
//            }

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
                System.out.println(customer+"this is my customer");
                otpService.deleteOtp(otp.getId());
                return "redirect:/login";
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

    @GetMapping("/forgot-password")
    public String getForgotPassword(){
        return "forgot-password";
    }

    @GetMapping("/otp-verification")
    public String otpVerification(Model model){
        return "otp-verification";
    }

    @PostMapping("/reset-user-password")
    public String resetUserPassword(@RequestParam("phoneNumber")String phoneNo,
                                    Model model
    ){
        try {
            String otp=otpService.generateOtp(phoneNo);
            Otp newOtp=new Otp();
            newOtp.setOtp(otp);
            newOtp.setPhoneNo(phoneNo);
            otpService.save(newOtp);
            System.out.println("otp sended"+phoneNo);
            model.addAttribute("phoneNumber",phoneNo);
            return "otp-verification";
        }catch (Exception e){
            e.printStackTrace();
            return "reset-password";
        }
    }

    @PostMapping("/verify-forgotPassword-otp")
    public String verifyForgotPasswordOtp(@RequestParam("otp") String otp,
                                          @RequestParam("phoneNo")String phoneNo,
                                          RedirectAttributes attributes
    ){
        try {
            Otp otp1=otpService.findByNumber(phoneNo);
            if(otp1.getOtp().equals(otp)){
                otpService.deleteOtp(otp1.getId());
                System.out.println("Password Verified");
                return "reset-password";
            }else {
                attributes.addFlashAttribute("error","Wrong otp");
                return "redirect:/forgot-password";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "redirect:/forgot-password";
        }
    }
    @GetMapping("/reset-password")
    public String resetPassword(){
        return "reset-password";
    }

    @PostMapping("/final-verification")
    public String finalVerification(@RequestParam("email")String email,
                                    @RequestParam("password")String password,
                                    @RequestParam("confirmPassword")String confirmPassword,
                                    RedirectAttributes attributes
    ){
        try {

            Customer customer=customerService.findByUsername(email);
            System.out.println("my email"+customer.getUsername());
            if(!customer.getUsername().equals(email)){
                attributes.addFlashAttribute("error","Incorrect email id");
                return "reset-password";
            }
            if(!password.equals(confirmPassword)){
                attributes.addFlashAttribute("error","Password is incorrect");
                return "reset-password";
            }
            customer.setPassword(passwordEncoder.encode(password));
            customerRepository.save(customer);
            Otp otp=otpService.findByNumber(customer.getPhoneNumber());
            otpService.deleteOtp(otp.getId());
            attributes.addFlashAttribute("success","Password Changed success fully");
            return "redirect:/login";
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error","Something went wrong please try again");
            return "reset-password";
        }
    }


}
