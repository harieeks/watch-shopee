package com.ecommerce.customer.Controller;

import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.model.Wishlist;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.service.ShoppingCartService;
import com.ecommerce.library.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ShoppingCartService cartService;

    @GetMapping("/wishlist")
    public String getWishlist(Principal principal, Model model){
        try {
            if(principal == null){
                return "redirect:/login";
            }
            Wishlist wishlist=wishlistService.findCustomerWishlist(principal.getName());
            if(wishlist == null || wishlist.getWishlistItems().size() == 0){
                return "redirect:/empty-wishlist";
            }

            model.addAttribute("wishlist",wishlist);
            model.addAttribute("size",wishlist.getWishlistItems().size());

            return "wishlist";
        }catch (Exception e){
            e.printStackTrace();
            return "wishlist";
        }
    }

    @PostMapping("/add-to-wishlist")
    @ResponseBody
    public String addItemToWishlist(@RequestParam Long product_id, Principal principal){
        try {
            if(principal == null){
                return "redirect:/login";
            }
            System.out.println("inside controller");
            String username=principal.getName();
            wishlistService.addToWishlist(username,product_id);
            return "success";
        }
        catch (Exception e){
            e.printStackTrace();
            return "redirect:/index";
        }
    }

    @RequestMapping(value = "/remove-wishlist-item/{id}",method = {RequestMethod.GET,RequestMethod.DELETE})
    public String removeWishlistItem(@PathVariable Long id,Principal principal){
        try {
            wishlistService.removeWishlistItem(principal.getName(), id);
            return "redirect:/wishlist";
        }catch (Exception e){
            e.printStackTrace();
            return "redirect:/wishlist";
        }
    }

    //@PostMapping("/add-toCart-from-wishlist/{id}")
    @RequestMapping(value = "/add-toCart-from-wishlist/{id}",method = {RequestMethod.GET,RequestMethod.POST})
    public String addToCartFromWishlist(@PathVariable Long id, Principal principal){

        try {
            Customer customer=customerService.findByUsername(principal.getName());
            Product product=productService.findById(id);
            cartService.addItemsToCart(product,1,customer);
            wishlistService.removeWishlistItem(principal.getName(),id);

            return "redirect:/wishlist";
        }catch (Exception e){
            e.printStackTrace();
            return "redirect:/wishlist";
        }
    }

    @GetMapping("/empty-wishlist")
    public String getEmptyWishlist(){
        return "emptyWishlist";
    }
}
