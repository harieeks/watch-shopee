package com.ecommerce.customer.Controller;

import com.ecommerce.library.Dto.ProductDto;
import com.ecommerce.library.model.Banner;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.service.BannerService;
import com.ecommerce.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;
    @Autowired
    private BannerService bannerService;

    @GetMapping("/index")
    public String getIndex(Model model,Principal principal){
        if(principal==null){
            return "redirect:/login";
        }
        List<ProductDto> products=productService.findAll();
        List<Banner> banners=bannerService.findAllBanners();
        model.addAttribute("product",products);
        model.addAttribute("banners",banners);
        return "index";
    }
}
