package com.ecommerce.customer.Controller;

import com.ecommerce.library.model.*;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.service.AddressService;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.OrderService;
import com.ecommerce.library.service.ProductService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.razorpay.RazorpayClient;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProductService productService;
    @Autowired
    private AddressService addressService;

    @PostMapping(value = "/add-order", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String addOrder(@RequestBody Map<String,Object> requestData,
                           Principal principal,
                           RedirectAttributes attributes
    )
    {
        System.out.println("Order initiated");
        try {

            if(requestData.get("houseName") == "" && requestData.get("selectedAddress") == null){
                return "success:true";
            }
            Customer customer=customerService.findByUsername(principal.getName());
            ShoppingCart cart=customer.getShoppingCart();

            String paymentMethod= (String) requestData.get("paymentMethod");
            String stringAddress=(String) requestData.get("selectedAddress");
            Long addressId=null;

            if(stringAddress != null && !stringAddress.isEmpty()){
                 addressId=Long.parseLong(stringAddress);
                System.out.println(addressId+"this is my address id changed");
            }
            if(requestData.get("houseName")!= ""){
                Address address=addressService.transferFormAddress(requestData,principal.getName());
                addressId=address.getId();
            }
            if("cash".equals(paymentMethod)){
                System.out.println("Inside order");
               Order order= orderService.save(cart,paymentMethod,addressId);
                System.out.println("order saved"+order.getId());

                return "cashOnDelivery";
            }
            else if("razorPay".equals(paymentMethod)){
                var client=new RazorpayClient("rzp_test_bX3HLV196l0ZXj","Opx9ExepcM0SwYu9LESYlOZh");
                JSONObject object=new JSONObject();
                object.put("amount",cart.getTotalPrice()*100);
                object.put("currency","INR");
                object.put("receipt","txn_20015");
                com.razorpay.Order order=client.orders.create(object);

                orderService.save(cart,paymentMethod,addressId);
                return order.toString();
            }

            return "done";
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error","Something went wrong Please try again");
            return "redirect:/checkout";
        }
    }

    @RequestMapping(value = "/cancel-order/{id}", method = {RequestMethod.GET,RequestMethod.POST})
    public String cancelOrder(@PathVariable("id")Long id,Principal principal){

        Order order=orderService.findOrderById(id);
        orderService.cancelOrder(order, principal.getName());
        return "redirect:/my-account";
    }

    @GetMapping("/confirmation")
    public String getOrderConfirmation(){
        return "confirmation";
    }
}
