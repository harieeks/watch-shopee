package com.ecommerce.admin.admin.Controller;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private CustomerService customerService;

//    @GetMapping("/customer")
//    public String getUserData(Model model){
//        try {
//            List<Customer> customers =customerService.findAllCustomer();
//            model.addAttribute("customers",customers);
//            model.addAttribute("size",customers.size());
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return "user-data";
//    }

    //@PutMapping("/block-user/{id}")
    @RequestMapping(value = "/block-user/{id}/{pageNo}",method = {RequestMethod.GET,RequestMethod.POST})
    public String blockuser(@PathVariable("id") Long id,@PathVariable("pageNo") int pageNo, RedirectAttributes attributes){
        try {
            customerService.blockUser(id);
            attributes.addFlashAttribute("success","Blocked Customer Success fully");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error","Failed to Block Customer ");
        }
        return "redirect:/customer/{pageNo}";
    }


    @RequestMapping(value = "/unblock-user/{id}/{pageNo}",method = {RequestMethod.GET,RequestMethod.POST})
    public String unBlockuser(@PathVariable("id") Long id,@PathVariable("pageNo")int pageNo, RedirectAttributes attributes){
        try {
            customerService.unblockUser(id);
            attributes.addFlashAttribute("success","Unblocked Customer Success fully");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error","Failed to Unblock Customer ");
        }
        return "redirect:/customer/{pageNo}";
    }

    @GetMapping("/customer/{pageNo}")
    public String productPage(@PathVariable("pageNo")int pageNo,Model model){
        String sortBy="name";

        Page<Customer> customers=customerService.pageCustomer(pageNo,sortBy);
        System.out.println("Customer Pagination"+customers);
        model.addAttribute("size",customers.getSize());
        model.addAttribute("totalPages",customers.getTotalPages());
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("customers",customers);
        return "user-data";
    }

    @GetMapping("/search-result/{pageNo}")
    public String searchCustomer(@PathVariable("pageNo")int pageNo,
                                 @RequestParam("keyword")String keyword,
                                 Model model
                                 ){
        Page<Customer> customers=customerService.searchCustomer(pageNo,keyword);
        model.addAttribute("customers",customers);
        model.addAttribute("totalPages",customers.getTotalPages());
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("size",customers.getSize());
        return "result-user-data";
    }


}
