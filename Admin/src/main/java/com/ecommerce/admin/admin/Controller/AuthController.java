package com.ecommerce.admin.admin.Controller;

import com.ecommerce.library.Dto.AdminDto;
import com.ecommerce.library.model.Admin;
import com.ecommerce.library.service.AdminService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public class AuthController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private  BCryptPasswordEncoder passwordEncoder;




    @GetMapping("/login")
    public String getLogin(){
        return "login";
    }

//    @GetMapping("/register")
//    public String getRegister(Model model){
//        model.addAttribute("adminDto",new AdminDto());
//        return "register";
//    }
//
//    @GetMapping("/forgot-password")
//    public String forgotPassword(Model model){
//        return "forgot-password";
//    }
//
//    @PostMapping("/register-new")
//    public String addNewAdmin(@Valid @ModelAttribute("adminDto")AdminDto adminDto,
//                              BindingResult result,
//                              Model model){
//        try {
//
//            if(result.hasErrors()){
//                model.addAttribute("adminDto",adminDto);
//                return "register";
//            }
//            Admin admin=adminService.findByUserName(adminDto.getEmail());
//            if(admin!=null){
//                model.addAttribute("adminDto",adminDto);
//                model.addAttribute("message","This email is all ready used");
//                model.addAttribute("emailError","This email is all ready used");
//                System.out.println("Adminexist");
//                return "register";
//            }
//            if(adminDto.getPassword().equals(adminDto.getRepeatPassword())){
//                adminDto.setPassword(passwordEncoder.encode(adminDto.getPassword()));
//                adminService.save(adminDto);
//                model.addAttribute("success","Registered successfully");
//                model.addAttribute("adminDto",adminDto);
//            }else{
//                model.addAttribute("adminDto",adminDto);
//                model.addAttribute("passwordError","Something wrong with your password");
//                System.out.println("passwordnotsame");
//                return "register";
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//            model.addAttribute("errors","Something went wrong");
//        }
//        return "register";
//    }
}
