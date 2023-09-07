package com.ecommerce.admin.admin.Controller;

import com.ecommerce.library.Dto.ProductDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Image;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/product")
    public String product(Model model, Principal principal){
        if(principal==null){
            return "redirect:/login";
        }
        //List<ProductDto> productDtoList=productService.findAll();
        List<Product> productList=productService.findAllProduct();
        model.addAttribute("products",productList);
        model.addAttribute("size",productList.size());
//        for(ProductDto productDto : productDtoList){
//            List<String> image=productDto.getImages();
//            for (String image1 = image){
//                System.out.println("images of product"+image1);
//            }
//
//        }
        return "product";
    }

    @GetMapping("/add-product")
    public String addProduct(Model model){
        List<Category> categories=categoryService.findAllByIsActivated();
        model.addAttribute("categoryNew",new Category());
        model.addAttribute("category",categories);
        model.addAttribute("product",new ProductDto());

      return "add-product";
    }
    @PostMapping("/add-product-category")
    public String addingCategoryWithProduct(@ModelAttribute("categoryNew") Category category,
                                            RedirectAttributes attributes
    ){
        try {
            categoryService.save(category);
            attributes.addFlashAttribute("success","Category Added");
        }catch (Exception e){
            attributes.addFlashAttribute("failed","Failed to Add");
        }
        return "redirect:/add-product";
    }

    @PostMapping("/save-product")
    public String saveProduct(@ModelAttribute("product")ProductDto productDto,
                              @RequestParam(value = "imageProduct",required = true)List<MultipartFile> imageProduct,
                              RedirectAttributes attributes
                              ){
        try {
            productService.save(imageProduct,productDto);
            attributes.addFlashAttribute("success","Added Success fully");

        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("failed","Failed to add");
        }

        return "redirect:/add-product";
    }

    @GetMapping("/update-product/{id}")
    public String updateProduct(@PathVariable("id")Long id, Model model){
        ProductDto productDto=productService.getById(id);
        List<Category> categories=categoryService.findAllByIsActivated();
        model.addAttribute("categories",categories);
        model.addAttribute("product",productDto);
        return "update-product";
    }

    @PostMapping("/doUpdate-product/{id}")
    public String doUpdate(@PathVariable("id")Long id,
                           @ModelAttribute("product")ProductDto productDto,
                           @RequestParam("imageProduct")List<MultipartFile> imageProduct,
                           RedirectAttributes attributes
    ){
        try {
            productService.update(imageProduct,productDto);
            attributes.addFlashAttribute("success","Updated Success fully");

        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error","Failed to update");
        }
        return "redirect:/product";
    }

//    @GetMapping("/delete-product/{id}")
//    public String deleteProduct(@PathVariable("id") Long id,RedirectAttributes attributes){
//        try {
//            productService.deleteById(id);
//            attributes.addFlashAttribute("success","Deleted Success fully");
//        }catch (Exception e){
//            e.printStackTrace();
//            attributes.addFlashAttribute("error","Failed to Delete");
//        }
//        return "redirect:/product";
//    }

    @GetMapping("/disable-product/{id}")
    public String disableProduct(@PathVariable("id")Long id,RedirectAttributes attributes){
        try {
            productService.disableById(id);
            attributes.addFlashAttribute("success","disabled Success fully");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error","Failed to disable");
        }

        return "redirect:/product";
    }

    @GetMapping("/enable-product/{id}")
    public String enableProduct(@PathVariable("id")Long id,RedirectAttributes attributes){
        try {
            productService.enableById(id);
            attributes.addFlashAttribute("success","Enable Success fully");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error","Failed to enable");
        }
        return "redirect:/product";
    }



}
