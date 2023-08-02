package com.ecommerce.customer.Controller;

import com.ecommerce.customer.Config.CustomerServiceConfig;
import com.ecommerce.library.Dto.ProductDto;
import com.ecommerce.library.model.*;
import com.ecommerce.library.service.BannerService;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

import java.util.List;
import java.util.Set;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;
    @Autowired
    private BannerService bannerService;
    @Autowired
    private CustomerService customerService;

    @GetMapping("/index")
    public String getIndex(Model model, Principal principal, HttpSession session){
        boolean name=false;
        if(principal !=null){
            session.setAttribute("username",principal.getName());
             name=true;
            model.addAttribute("name",name);
        }
        List<ProductDto> products=productService.listViewProduct();
        List<Banner> banners=bannerService.findAllBanners();

        if(principal != null){
            Customer customer=customerService.findByUsername(principal.getName());
            ShoppingCart cart=customer.getShoppingCart();
            Set<CartItem> cartItem=cart.getCartItem();
            model.addAttribute("cartItem",cartItem.size());
        } else model.addAttribute("cartItem",0);



        model.addAttribute("product",products);
        model.addAttribute("banners",banners);
        model.addAttribute("name",name);
        return "index";
    }

    @GetMapping("/product-view")
    public String productView(@RequestParam("id") Long id,Model model,Principal principal){
        try{
            ProductDto productDto=productService.getById(id);
            System.out.println(productDto);
            if(principal !=null){
                Customer customer=customerService.findByUsername(principal.getName());
                ShoppingCart cart=customer.getShoppingCart();
                Set<CartItem> cartItems=cart.getCartItem();
                model.addAttribute("cartItem",cartItems.size());
            } else model.addAttribute("cartItem",0);

            model.addAttribute("product",productDto);
            return "product-detail";
        }catch (Exception e){
            e.printStackTrace();
            return "login";

        }
    }


}
