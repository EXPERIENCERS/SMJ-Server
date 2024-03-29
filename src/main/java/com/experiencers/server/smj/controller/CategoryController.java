package com.experiencers.server.smj.controller;

import com.experiencers.server.smj.domain.Category;
import com.experiencers.server.smj.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class CategoryController{
    @Autowired
    CategoryService categoryService;

    @GetMapping("/category")
    public ModelAndView getCategories(){
        List<Category> categories = categoryService.readAllCategory();

        ModelAndView response = new ModelAndView("category/index");
        response.addObject("categories", categories);

        return response; // category를 연결하면
    }

    @PostMapping("/category")
    public String postCategory(@ModelAttribute Category inputtedCategory) {
        categoryService.saveCategoryOfMember(inputtedCategory);

        return "redirect:/admin/category";
    }

    @GetMapping("/category/{category_id}/edit")
    public ModelAndView editCategory(@PathVariable("category_id") Long categoryId) {
        Category category = categoryService.readCategory(categoryId);

        ModelAndView mav = new ModelAndView("category/edit");
        mav.addObject("category", category);
        return mav;
    }

    @PostMapping("/category/{category_id}/update")
    public String updateCategory(@PathVariable("category_id") Long categoryId,
                                       @ModelAttribute Category category) {
        categoryService.readAndUpdateCategoryOfMember(categoryId, category);

        return "redirect:/admin/category";
    }

    @PostMapping("/category/{category_id}/delete")
    public String deleteCategory(@PathVariable("category_id") Long categoryId) {
        categoryService.deleteCategory(categoryId);

        return "redirect:/admin/category";
    }
}
