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
    public String add(@ModelAttribute("categoryNew")Category category,
                      RedirectAttributes attributes){
        try {
            List<Category> oldCategory=categoryService.findAll();
            if(oldCategory.contains(category.getName())){
                System.out.println(category.getName()+"NotAdded");
                throw new CategoryAllreadyExist();
            }else{
                categoryService.save(category);
                System.out.println("Added");
                attributes.addFlashAttribute("success","Added Successfully");
            }
        }catch (CategoryAllreadyExist p){
            attributes.addFlashAttribute("error","This Category all ready exist");
        }
        catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("failed","Failed to add");
        }
        return "redirect:/category";
    }

    @GetMapping("/update-category")
    public String updateCategory(Category category,RedirectAttributes attributes){
        try {
            categoryService.update(category);
            attributes.addFlashAttribute("Success","Updated successfully");

        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("Failed","Failed to update");
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
