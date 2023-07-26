package com.ecommerce.admin.admin.Controller;

import com.ecommerce.library.model.Banner;
import com.ecommerce.library.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @GetMapping("/banner")
    public String getBanner(Model model, Principal principal){
        if(principal==null){
            return "/login";
        }try {
            List<Banner> banners=bannerService.findAllBanners();
            model.addAttribute("size",banners.size());
            model.addAttribute("banners",banners);
            return "banner";
        }catch (Exception e){
            e.printStackTrace();
        }
      return null;
    }

    @PostMapping("/add-banner")
    public String addBanner(@RequestParam(value = "bannerImage",required = true)MultipartFile bannerImage,
                            RedirectAttributes attributes
    ){
        try {
            bannerService.save(bannerImage,new Banner());
            attributes.addFlashAttribute("success","Success fully added");
            return "redirect:/banner";
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error","Something went wrong");
        }
        return "redirect:/banner";
    }

    @GetMapping("/delete-banner/{id}")
    public String deleteBanner(@PathVariable("id")Long id){
        bannerService.deleteBanner(id);
        return "redirect:/banner";
    }
}
