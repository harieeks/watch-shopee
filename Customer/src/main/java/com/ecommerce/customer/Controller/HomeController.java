package com.ecommerce.customer.Controller;

import com.ecommerce.customer.Config.CustomerServiceConfig;
import com.ecommerce.library.Dto.ImageDto;
import com.ecommerce.library.Dto.ProductDto;
import com.ecommerce.library.model.*;
import com.ecommerce.library.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    @Autowired
    private ImageService imageService;
    @Autowired
    private ProductFeedbackService productFeedbackService;


    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String getIndex(Model model, Principal principal){
        boolean name=false;
        if(principal !=null){
             name=true;
            model.addAttribute("name",name);
        }
        List<ProductDto> products=productService.listViewProduct();
        List<Banner> banners=bannerService.findAllBanners();
        Product analog=productService.getSingleAnalogProduct();
        Product digital=productService.getSingleDigitalProduct();
        Product smart=productService.getSingleSmartProduct();

        if(principal != null){
            Customer customer=customerService.findByUsername(principal.getName());
            ShoppingCart cart=customer.getShoppingCart();
            Wishlist wishlist=customer.getWishlist();
            if(cart == null){
                model.addAttribute("cartItem",0);
            }else{
                Set<CartItem> cartItem=cart.getCartItem();
                model.addAttribute("cartItem",cartItem.size());
            }
            if( wishlist == null) model.addAttribute("wishlist",0);
            else{
                Set<WishlistItem> wishlistItems=wishlist.getWishlistItems();
                model.addAttribute("wishlist",wishlistItems.size());
            }
        } else {
            model.addAttribute("cartItem",0);
            model.addAttribute("wishlist",0);
        }

        model.addAttribute("product",products);
        model.addAttribute("banners",banners);
        model.addAttribute("name",name);
        model.addAttribute("analog",analog);
        model.addAttribute("digital",digital);
        model.addAttribute("smart",smart);
        return "index";
    }

    @GetMapping("/product-view")
    public String productView(@RequestParam("id") Long id,Model model,Principal principal){
        try{
            ProductDto productDto=productService.getById(id);
            List<Image> images=imageService.findImageByProductId(id);
//            ProductDto productDto1=imageService.productImages(id);
//            List<Image> images=productDto1.getImages();

            int productQuantity=productDto.getCurrentQuantity();
            ProductDto feedbackDto=productFeedbackService.getProductFeedback(id);
            List<ProductFeedback> feedbacks=feedbackDto.getFeedbacks();


            if(principal !=null){
                Customer customer=customerService.findByUsername(principal.getName());
                ShoppingCart cart=customer.getShoppingCart();
                Wishlist wishlist=customer.getWishlist();

                if(cart == null){
                    model.addAttribute("cartItem",0);
                }else{
                    Set<CartItem> cartItems=cart.getCartItem();
                    model.addAttribute("cartItem",cartItems.size());
                }
                if(wishlist == null){
                    model.addAttribute("wishlist",0);
                }else{
                    Set<WishlistItem> wishlistItems=wishlist.getWishlistItems();
                    model.addAttribute("wishlist",wishlistItems.size());
                }
            } else {
                model.addAttribute("cartItem",0);
                model.addAttribute("wishlist",0);
            }
            if(feedbacks != null){
                model.addAttribute("feedback",feedbacks);
            }else{
                model.addAttribute("feedback","No feedbacks for this product");
            }

            model.addAttribute("product",productDto);
            model.addAttribute("image",images);
            model.addAttribute("quantity",productQuantity);
            return "product-detail";
        }catch (Exception e){
            e.printStackTrace();
            return "login";
        }
    }
    @GetMapping("/smart-watch")
    public String getSmartWatch(Model model,Principal principal){
        try {
            boolean name=false;
            if(principal !=null){
                name=true;
                model.addAttribute("name",name);
            }
            List<Product> products=productService.getSmartWatch();
            if(principal !=null){
                Customer customer=customerService.findByUsername(principal.getName());
                ShoppingCart cart=customer.getShoppingCart();
                Wishlist wishlist=customer.getWishlist();

                if(cart == null){
                    model.addAttribute("cartItem",0);
                }else{
                    Set<CartItem> cartItems=cart.getCartItem();
                    model.addAttribute("cartItem",cartItems.size());
                }
                if(wishlist == null){
                    model.addAttribute("wishlist",0);
                }else{
                    Set<WishlistItem> wishlistItems=wishlist.getWishlistItems();
                    model.addAttribute("wishlist",wishlistItems.size());
                }
            } else {
                model.addAttribute("cartItem",0);
                model.addAttribute("wishlist",0);
            }
            model.addAttribute("product",products);
            model.addAttribute("result",true);

        }catch (Exception e){
            e.printStackTrace();
        }
      return "smart-watch";
    }

    @GetMapping("/digital-watch")
    public String getDigitalWatch(Model model,Principal principal){
        try {
            boolean name=false;
            if(principal !=null){
                name=true;
                model.addAttribute("name",name);
            }
            List<Product> products=productService.getDigitalWatch();
            if(principal !=null){
                Customer customer=customerService.findByUsername(principal.getName());
                ShoppingCart cart=customer.getShoppingCart();
                Wishlist wishlist=customer.getWishlist();

                if(cart == null){
                    model.addAttribute("cartItem",0);
                }else{
                    Set<CartItem> cartItems=cart.getCartItem();
                    model.addAttribute("cartItem",cartItems.size());
                }
                if(wishlist == null){
                    model.addAttribute("wishlist",0);
                }else{
                    Set<WishlistItem> wishlistItems=wishlist.getWishlistItems();
                    model.addAttribute("wishlist",wishlistItems.size());
                }
            } else {
                model.addAttribute("cartItem",0);
                model.addAttribute("wishlist",0);
            }
            model.addAttribute("product",products);
            model.addAttribute("result",true);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "digital-watch";
    }
    @GetMapping("/analog-watch")
    public String getAnalogWatch(Model model,Principal principal){
        try {
            boolean name=false;
            if(principal !=null){
                name=true;
                model.addAttribute("name",name);
            }

            List<Product> products=productService.getAnalogWatch();
            if(principal !=null){
                Customer customer=customerService.findByUsername(principal.getName());
                ShoppingCart cart=customer.getShoppingCart();
                Wishlist wishlist=customer.getWishlist();

                if(cart == null){
                    model.addAttribute("cartItem",0);
                }else{
                    Set<CartItem> cartItems=cart.getCartItem();
                    model.addAttribute("cartItem",cartItems.size());
                }
                if(wishlist == null){
                    model.addAttribute("wishlist",0);
                }else{
                    Set<WishlistItem> wishlistItems=wishlist.getWishlistItems();
                    model.addAttribute("wishlist",wishlistItems.size());
                }
            } else {
                model.addAttribute("cartItem",0);
                model.addAttribute("wishlist",0);
            }
            model.addAttribute("product",products);
            model.addAttribute("result",true);

        }catch (Exception e){
            e.printStackTrace();
        }
        return "analog-watch";
    }
    @GetMapping("/search-analog")
    public String searchProduct(@RequestParam(value = "search-product") String keyword, Model model,Principal principal){
        try {
            boolean name=false;
            if(principal !=null){
                name=true;
                model.addAttribute("name",name);
            }
            List<Product>products=productService.listAnalog(keyword);
            if(products.isEmpty()){
                model.addAttribute("noResultsMessage", "No products found for the given keyword.");

            }else{
                model.addAttribute("product",products);
                model.addAttribute("result",true);
            }
            if(principal !=null){
                Customer customer=customerService.findByUsername(principal.getName());
                ShoppingCart cart=customer.getShoppingCart();
                Wishlist wishlist=customer.getWishlist();

                if(cart == null){
                    model.addAttribute("cartItem",0);
                }else{
                    Set<CartItem> cartItems=cart.getCartItem();
                    model.addAttribute("cartItem",cartItems.size());
                }
                if(wishlist == null){
                    model.addAttribute("wishlist",0);
                }else{
                    Set<WishlistItem> wishlistItems=wishlist.getWishlistItems();
                    model.addAttribute("wishlist",wishlistItems.size());
                }
            } else {
                model.addAttribute("cartItem",0);
                model.addAttribute("wishlist",0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return "analog-watch";
    }

    @GetMapping("/search-digital")
    public String searchDigital(@RequestParam(value = "search-product") String keyword, Model model,Principal principal){
        try {
            boolean name=false;
            if(principal !=null){
                name=true;
                model.addAttribute("name",name);
            }
            List<Product>products=productService.listDigital(keyword);
            if(products.isEmpty()){
                model.addAttribute("noResultsMessage", "No products found for the given keyword.");

            }else{
                model.addAttribute("product",products);
                model.addAttribute("result",true);
            }
            if(principal !=null){
                Customer customer=customerService.findByUsername(principal.getName());
                ShoppingCart cart=customer.getShoppingCart();
                Wishlist wishlist=customer.getWishlist();

                if(cart == null){
                    model.addAttribute("cartItem",0);
                }else{
                    Set<CartItem> cartItems=cart.getCartItem();
                    model.addAttribute("cartItem",cartItems.size());
                }
                if(wishlist == null){
                    model.addAttribute("wishlist",0);
                }else{
                    Set<WishlistItem> wishlistItems=wishlist.getWishlistItems();
                    model.addAttribute("wishlist",wishlistItems.size());
                }
            } else {
                model.addAttribute("cartItem",0);
                model.addAttribute("wishlist",0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "digital-watch";
    }
    @GetMapping("/search-smart")
    public String searchSmart(@RequestParam(value = "search-product") String keyword, Model model,Principal principal){
        try {
            boolean name=false;
            if(principal !=null){
                name=true;
                model.addAttribute("name",name);
            }
            List<Product>products=productService.listSmart(keyword);
            if(products.isEmpty()){
                model.addAttribute("noResultsMessage", "No products found for the given keyword.");

            }else{
                model.addAttribute("product",products);
                model.addAttribute("result",true);
            }
            if(principal !=null){
                Customer customer=customerService.findByUsername(principal.getName());
                ShoppingCart cart=customer.getShoppingCart();
                Wishlist wishlist=customer.getWishlist();

                if(cart == null){
                    model.addAttribute("cartItem",0);
                }else{
                    Set<CartItem> cartItems=cart.getCartItem();
                    model.addAttribute("cartItem",cartItems.size());
                }
                if(wishlist == null){
                    model.addAttribute("wishlist",0);
                }else{
                    Set<WishlistItem> wishlistItems=wishlist.getWishlistItems();
                    model.addAttribute("wishlist",wishlistItems.size());
                }
            } else {
                model.addAttribute("cartItem",0);
                model.addAttribute("wishlist",0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "smart-watch";
    }

    @GetMapping("/analog-filter")
    public String getAnalogFilter(@RequestParam("minPrice") double minPrice,
                                  @RequestParam("maxPrice") double maxPrice,
                                  Model model,
                                  Principal principal){
        try {
            boolean name=false;
            if(principal !=null){
                name=true;
                model.addAttribute("name",name);
            }
            List<Product>products=productService.findAnalogProductByPrice(minPrice,maxPrice);
            if(products.isEmpty()){
                model.addAttribute("noResultsMessage", "No products found.");

            }else{
                model.addAttribute("product",products);
                model.addAttribute("result",true);
            }
            if(principal !=null){
                Customer customer=customerService.findByUsername(principal.getName());
                ShoppingCart cart=customer.getShoppingCart();
                Wishlist wishlist=customer.getWishlist();

                if(cart == null){
                    model.addAttribute("cartItem",0);
                }else{
                    Set<CartItem> cartItems=cart.getCartItem();
                    model.addAttribute("cartItem",cartItems.size());
                }
                if(wishlist == null){
                    model.addAttribute("wishlist",0);
                }else{
                    Set<WishlistItem> wishlistItems=wishlist.getWishlistItems();
                    model.addAttribute("wishlist",wishlistItems.size());
                }
            } else {
                model.addAttribute("cartItem",0);
                model.addAttribute("wishlist",0);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return "analog-watch";
    }
    @GetMapping("/digital-filter")
    public String getDigitalFilter(@RequestParam("minPrice") double minPrice,
                                  @RequestParam("maxPrice") double maxPrice,
                                  Model model,
                                  Principal principal){
        try {
            boolean name=false;
            if(principal !=null){
                name=true;
                model.addAttribute("name",name);
            }
            List<Product>products=productService.findDigitalProductByPrice(minPrice, maxPrice);
            if(products.isEmpty()){
                model.addAttribute("noResultsMessage", "No products found.");

            }else{
                model.addAttribute("product",products);
                model.addAttribute("result",true);
            }
            if(principal !=null){
                Customer customer=customerService.findByUsername(principal.getName());
                ShoppingCart cart=customer.getShoppingCart();
                Wishlist wishlist=customer.getWishlist();

                if(cart == null){
                    model.addAttribute("cartItem",0);
                }else{
                    Set<CartItem> cartItems=cart.getCartItem();
                    model.addAttribute("cartItem",cartItems.size());
                }
                if(wishlist == null){
                    model.addAttribute("wishlist",0);
                }else{
                    Set<WishlistItem> wishlistItems=wishlist.getWishlistItems();
                    model.addAttribute("wishlist",wishlistItems.size());
                }
            } else {
                model.addAttribute("cartItem",0);
                model.addAttribute("wishlist",0);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return "digital-watch";
    }
    @GetMapping("/smart-filter")
    public String getSmartFilter(@RequestParam("minPrice") double minPrice,
                                  @RequestParam("maxPrice") double maxPrice,
                                  Model model,
                                  Principal principal){
        try {
            boolean name=false;
            if(principal !=null){
                name=true;
                model.addAttribute("name",name);
            }
            List<Product>products=productService.findSmartProductByPrice(minPrice, maxPrice);
            if(products.isEmpty()){
                model.addAttribute("noResultsMessage", "No products found.");

            }else{
                model.addAttribute("product",products);
                model.addAttribute("result",true);
            }
            if(principal !=null){
                Customer customer=customerService.findByUsername(principal.getName());
                ShoppingCart cart=customer.getShoppingCart();
                Wishlist wishlist=customer.getWishlist();

                if(cart == null){
                    model.addAttribute("cartItem",0);
                }else{
                    Set<CartItem> cartItems=cart.getCartItem();
                    model.addAttribute("cartItem",cartItems.size());
                }
                if(wishlist == null){
                    model.addAttribute("wishlist",0);
                }else{
                    Set<WishlistItem> wishlistItems=wishlist.getWishlistItems();
                    model.addAttribute("wishlist",wishlistItems.size());
                }
            } else {
                model.addAttribute("cartItem",0);
                model.addAttribute("wishlist",0);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return "smart-watch";
    }

    @RequestMapping(value = "/add-feedback",method = {RequestMethod.POST,RequestMethod.GET})
    public String addFeedback(Principal principal,
                              @RequestParam("feedbackProductId")Long id,
                              @RequestParam("feedback")String feedback,
                              RedirectAttributes attributes
                              ){

        try {
            if(principal == null){
                return "redirect:/login";
            }
            productFeedbackService.saveReview(feedback,id);
            attributes.addFlashAttribute("success","Thanks for your feedback");
            return "redirect:/product-view?id="+id;
        }catch (Exception e){
            attributes.addFlashAttribute("error","An error occur while saving your feedback");
            return "redirect:/product-view?id="+id;
        }
    }



}
