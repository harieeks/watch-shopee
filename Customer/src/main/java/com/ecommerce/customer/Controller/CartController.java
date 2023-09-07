package com.ecommerce.customer.Controller;

import com.ecommerce.library.Dto.ProductDto;
import com.ecommerce.library.model.*;
import com.ecommerce.library.service.AddressService;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.service.ShoppingCartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
public class CartController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private AddressService addressService;

    @GetMapping("/cart")
    public String getCart(Principal principal, Model model){
        if(principal==null){
            return "redirect:/login";
        }
        String username= principal.getName();
        Customer customer=customerService.findByUsername(username);
        Address address=addressService.findAddressByCustomerId(customer.getId());
        if(address != null){
            model.addAttribute("address",address);
        } else {
            model.addAttribute("address",null);

        }
        ShoppingCart cart=customer.getShoppingCart();
        if(cart == null || cart.getCartItem().size() == 0){
            return "redirect:/empty-cart";
        }
        if(cart != null){
            Set<CartItem> cartItem=cart.getCartItem();
            model.addAttribute("cartItems",cartItem.size());
        }else{
            model.addAttribute("cartItems",0);
        }
        if(cart == null){
            model.addAttribute("check","No items in your cart");
        }
        model.addAttribute("shoppingCart",cart);

        return "shoping-cart";
    }

    @GetMapping("/empty-cart")
    public String getEmptyCart(){
        return "emptyCart";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam("id") Long id,
                            @RequestParam(value = "quantity",required = false,defaultValue = "1") int quantity,
                            Principal principal,
                            Model model,
                            HttpServletRequest request,
                            RedirectAttributes attributes
                            ){
        try {
            if(principal == null){
                return "redirect:/login";
            }
            Product product=productService.findById(id);
            String username=principal.getName();
            Customer customer=customerService.findByUsername(username);

            if(product.getCurrentQuantity()<quantity){
                attributes.addFlashAttribute("error","Entered quantity is not available");
                return "redirect:"+request.getHeader("referer");
            }

            ShoppingCart cart=shoppingCartService.addItemsToCart(product,quantity,customer);
            attributes.addFlashAttribute("success","Added successfully");
            return "redirect:"+request.getHeader("referer");

        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("error","Something went wrong");
            return "redirect:"+request.getHeader("referer");
        }
    }

    @PostMapping("/update-cart")
    public String updateCart(@RequestParam("num-product1")int quantity,
                             @RequestParam("id")Long id,
                             Principal principal,
                             Model model,
                             RedirectAttributes attributes
    ){
        try {
            if(principal == null){
                return "redirect:/login";
            }
            System.out.println("in cart router");
            String username= principal.getName();
            Customer customer=customerService.findByUsername(username);
            Product product=productService.findById(id);

            if(product.getCurrentQuantity()<quantity){
                attributes.addFlashAttribute("error","Mentioned quantity is not available");
                return "redirect:/cart";
            }

            ShoppingCart cart=shoppingCartService.updateItemsInCart(product,quantity,customer);
            model.addAttribute("shoppingCart",cart);
            return "redirect:/cart";

        }catch (Exception e){
            e.printStackTrace();
            return "redirect:/cart";
        }
    }

//    @DeleteMapping("/remove-item")
    @RequestMapping(value = "/remove-item/{id}",method = {RequestMethod.GET,RequestMethod.DELETE})
    public  String removeItem(@PathVariable("id") Long id,
                              Principal principal
                              ){
        String username=principal.getName();
        Customer customer=customerService.findByUsername(username);
        Product product=productService.findById(id);
        ShoppingCart cart=shoppingCartService.deleteItemFromCart(product,customer);
        return "redirect:/cart";
    }

//    @PostMapping("/remove-item")
//    @ResponseBody
//    public Map<String, Object> removeItemAjax(@RequestParam("productid") Long id, Principal principal) {
//        Map<String, Object> response = new HashMap<>();
//
//        String username = principal.getName();
//        Customer customer = customerService.findByUsername(username);
//        Product product = productService.findById(id);
//        ShoppingCart cart = shoppingCartService.deleteItemFromCart(product, customer);
//
//        if (cart != null) {
//            response.put("status", "success");
//        } else {
//            response.put("status", "error");
//        }
//
//        return response;
//    }
}
