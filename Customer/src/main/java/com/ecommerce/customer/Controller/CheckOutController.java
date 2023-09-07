package com.ecommerce.customer.Controller;

import com.ecommerce.library.model.*;
import com.ecommerce.library.repository.ShoppingCartRepository;
import com.ecommerce.library.repository.WalletRepository;
import com.ecommerce.library.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Controller
public class CheckOutController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private ShoppingCartRepository cartRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private WalletService walletService;
    @Autowired
    private CouponService couponService;

    @GetMapping("/checkout")
    public String getCheckOut(Principal principal, Model model){
        if(principal == null){
            return "redirect:/login";
        }
        Customer customer=customerService.findByUsername(principal.getName());
        ShoppingCart cart=customer.getShoppingCart();
        Wallet wallet=walletService.findCustomerWallet(customer.getId());
        if(wallet != null){
            model.addAttribute("wallet",wallet.getAmount());
        }
        if(cart!= null){
           Set<CartItem> cartItem=cart.getCartItem();
            model.addAttribute("shoppingCart",cart);
//            System.out.println("this is my  controller"+cartItem);
//            System.out.println("this is my  controller"+cart.getCartItem());
//            for(CartItem cartItem1:cartItem){
//                System.out.println("my items"+cartItem1);
//            }

        }else{
            model.addAttribute("cart",null);
            return "redirect:/cart";
        }
        List<Address> address=customer.getAddresses();
        Address randomAddress=addressService.findAddressByCustomerId(customer.getId());
        model.addAttribute("newAddress",new Address());
        if(randomAddress != null){
            model.addAttribute("randomAddress",randomAddress);
        }
        if(address != null){
            model.addAttribute("address",address);
        }else{
            return "redirect:/my-account";
        }
        return "checkout";
    }

    @RequestMapping(value = "/use-wallet-money",method = {RequestMethod.GET,RequestMethod.POST})
    public String useWalletMoney(Principal principal){

        Customer customer=customerService.findByUsername(principal.getName());
        ShoppingCart cart=customer.getShoppingCart();
        Wallet wallet=customer.getWallet();
        try {
            double amount1= cart.getTotalPrice();
            if(cart.getTotalPrice()>= wallet.getAmount()){
                cart.setTotalPrice(cart.getTotalPrice() - wallet.getAmount());
                cartRepository.save(cart);
                wallet.setAmount((double) 0);
            }else{
                cart.setTotalPrice(0);
                cartRepository.save(cart);
                wallet.setAmount(wallet.getAmount() - amount1);
            }
            walletRepository.save(wallet);
            return "redirect:/checkout";

        }catch (NullPointerException e){
            e.printStackTrace();
            return "redirect:/checkout";
        }catch (Exception e){
            e.printStackTrace();
            return "redirect:/checkout";
        }
    }

    @RequestMapping(value = "/apply-coupon",method = {RequestMethod.GET,RequestMethod.POST})
    public String applyCoupon(@RequestParam("coupon") String couponCode,
                              Principal principal,
                              RedirectAttributes attributes){
        try {
            Coupon coupon=couponService.findCouponByCouponCode(couponCode);
            Customer customer=customerService.findByUsername(principal.getName());
            ShoppingCart cart=customer.getShoppingCart();
            if(coupon == null || !couponCode.equals(coupon.getCouponCode())){
                attributes.addFlashAttribute("error","Coupon code is incorrect");
                return "redirect:/checkout";
            }
            if( coupon.getIsActive() == true && coupon.getExpiry().after(new Date())){
                if(cart.getTotalPrice() >= coupon.getCouponDiscount()){
                    cart.setTotalPrice(cart.getTotalPrice()-coupon.getCouponDiscount());
                    cartRepository.save(cart);
                }else{
                    attributes.addFlashAttribute("error",
                            "The Coupon is only applicable for the order greater than coupon discount");
                }
            }else {
                attributes.addFlashAttribute("error","This coupon is expired!!");
                return "redirect:/checkout";
            }
        }catch (NullPointerException nE){
            nE.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error","Something went wrong");
        }
        return "redirect:/checkout";
    }
}
