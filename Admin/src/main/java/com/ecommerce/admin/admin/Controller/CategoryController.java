package com.ecommerce.admin.admin.Controller;

import com.ecommerce.admin.admin.Exception.CategoryAllreadyExist;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/category")
    public String getCategory(Model model, Principal principal){
        if(principal==null){
            return "redirect:/login";
        }
        List<Category> categories=categoryService.findAll();
        model.addAttribute("categories",categories);
        model.addAttribute("size",categories.size());
        model.addAttribute("categoryNew",new Category());
        return "category";
    }
    @ExceptionHandler(value = CategoryAllreadyExist.class)

    @PostMapping("/add-category")
    public String add(@ModelAttribute("categoryNew") Category category,
                      RedirectAttributes attributes) {
        try {
            Category category1=categoryService.save(category);
            if(category1 !=null){
                attributes.addFlashAttribute("success", "Added Successfully");
            }else {
                attributes.addFlashAttribute("error", "Failed to add");
            }
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to add");
        }
        return "redirect:/category";
    }


    @RequestMapping(value = "/findById", method = {RequestMethod.PUT,RequestMethod.GET})
    @ResponseBody
    public Category findById(Long id){
       return categoryService.findById(id);
    }

    @GetMapping("/update-category")
    public String updateCategory(Category category,RedirectAttributes attributes){
        try {
           categoryService.update(category);
            attributes.addFlashAttribute("Success","Updated successfully");

        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error","Failed to update");
        }
        return "redirect:/category";
    }


    @RequestMapping(value = "/delete-category",method = {RequestMethod.GET,RequestMethod.PUT})
    public String delete(Long id,RedirectAttributes atrAttributes){
        try {
            categoryService.deleteById(id);
            atrAttributes.addFlashAttribute("success","Disabled");
        }catch (Exception e){
            e.printStackTrace();
            atrAttributes.addFlashAttribute("failed","Failed");
        }
        return "redirect:/category";

    }

    @RequestMapping(value = "enable-category",method = {RequestMethod.PUT,RequestMethod.GET})
    public String enableCategory(Long id,RedirectAttributes attributes){
        try {
            categoryService.enableById(id);
            attributes.addFlashAttribute("success","Enabled");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("failed","Failed");
        }
        return "redirect:/category";
    }
}
