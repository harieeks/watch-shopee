package com.ecommerce.customer.Controller;

import com.ecommerce.library.Dto.AddressDto;
import com.ecommerce.library.Dto.CustomerDto;
import com.ecommerce.library.Dto.OrderDto;
import com.ecommerce.library.model.Address;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.Wallet;
import com.ecommerce.library.service.AddressService;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.OrderService;
import com.ecommerce.library.service.WalletService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.*;

@Controller
public class CustomerProfileController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private OrderService orderService;
    @Autowired
    private WalletService walletService;

    @GetMapping("/my-account")
    public String getMyAccount(Model model, Principal principal){
        try{
            if(principal ==null){
                return "redirect:/login";
            }

            String username= principal.getName();
            Customer customer= customerService.findByUsername(username);
            String name= customer.getName();
//            List<Order> orderList=customer.getOrders();
            List<OrderDto> orderList=orderService.getOrders(customer);
            Wallet wallet=walletService.findCustomerWallet(customer.getId());

            if(orderList !=null){
                model.addAttribute("orders",orderList);
            }else {
                model.addAttribute("size",orderList.size());
            }
            List<Address> addressList=customer.getAddresses();
            if(addressList != null){
                model.addAttribute("addressList",addressList);
            }else {
                model.addAttribute("addressList",null);
            }
            //Address randomAddress=addressService.findAddressByCustomerId(customer.getId());
            model.addAttribute("name",name);
            model.addAttribute("phoneNo",customer.getPhoneNumber());
            model.addAttribute("email",customer.getUsername());
            model.addAttribute("customer",customer);
            model.addAttribute("address",new Address());
            model.addAttribute("wallet",wallet.getAmount());

            return "account";
        }catch (Exception e){
            e.printStackTrace();
            return "account";
        }
    }

    @PostMapping("/add-address")
    public String getAddress(@ModelAttribute("address")Address address,Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
        String username= principal.getName();
        System.out.println("This is my address");
        addressService.addAddress(address,username);
        return "redirect:/my-account";
    }

    @RequestMapping(value = "/remove-address/{id}",method = {RequestMethod.GET})
    public String deleteAddress(@PathVariable("id") Long id,Principal principal,RedirectAttributes attributes){
        Address address=addressService.getById(id);
        Set<Address> addressSet=new HashSet<>();
        if(principal != null){
            Customer customer=customerService.findByUsername(principal.getName());
            List<Order> orders=customer.getOrders();
            for(Order order:orders){
                addressSet.add(order.getAddress());
            }
        }
        if(addressSet != null){
            if(!addressSet.contains(address)){
                addressService.deleteAddress(id);
            }else{
               attributes.addFlashAttribute("error","You can't remove this Address because it is used for order");
            }
        }
        return "redirect:/my-account";
    }

    @RequestMapping(value = "/find-customerAddress",method = {RequestMethod.PUT,RequestMethod.GET})
    @ResponseBody
    public AddressDto findCustomerAddress( Long id){
        return addressService.findById(id);
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("currentPassword")String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword")String confirmPassword,
                                 RedirectAttributes attributes,
                                 Model model,
                                 Principal principal
                                )
    {
        try {
            if(principal == null){
                return "redirect:/login";
            }
            Customer customer=customerService.findByUsername(principal.getName());
            if(passwordEncoder.matches(currentPassword,customer.getPassword())
                    && !passwordEncoder.matches(newPassword,currentPassword)
                    && !passwordEncoder.matches(newPassword,customer.getPassword())
                    && confirmPassword.equals(newPassword) && newPassword.length()>=4
            ){
                customer.setPassword(passwordEncoder.encode(newPassword));
                Customer customer1=customerService.changePassword(customer);
                attributes.addFlashAttribute("success","password Changed Success fully");
            }else {
                attributes.addFlashAttribute("error","Your Password is wrong");
            }
            return "redirect:/my-account";
        }catch (Exception e){
            e.printStackTrace();
            return "redirect:/my-account";
        }
    }

    @GetMapping("/edit-address")
    public String editAddress(Address address,RedirectAttributes attributes,Principal principal){
        try {
            if(principal!=null){
                System.out.println("This is my address is"+address.getId());
                addressService.updateAddress(address, principal.getName());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/my-account";
    }
    @GetMapping("/track-order")
    public String trackOrder(@RequestParam("orderId")Long orderId,Model model){
        try {
            Order order=orderService.findOrderById(orderId);
            model.addAttribute("order",order);
            model.addAttribute("orderStatus",order.getOrderStatus());
        }catch (Exception e){
            e.printStackTrace();
        }
        return "track-order";
    }

}
