package com.ecommerce.admin.admin.Controller;

import com.ecommerce.library.model.Order;
import com.ecommerce.library.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public String getCustomerOrders(Principal principal, Model model){
        if(principal == null)return "redirect:/login";
        List<Order> orders=orderService.findAllOrders();
        model.addAttribute("orders",orders);
        return "orders";
    }

    @PostMapping("/update-order-status/{id}")
    public String updateOrderStatus(@PathVariable("id")Long id,
                                    @RequestParam("selectedStatus")int statusId
    ){
        String status="";
        if(statusId == 1)status="Delivered";
        else if (statusId == 2) status="Shipping";
        else status="Pending";
        orderService.updateOrderStatus(id,status);
        return "redirect:/orders";
    }


}
