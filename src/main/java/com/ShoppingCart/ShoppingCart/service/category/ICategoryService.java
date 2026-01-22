package com.ShoppingCart.ShoppingCart.service.category;

import com.ShoppingCart.ShoppingCart.entity.Category;

import java.util.List;

public interface ICategoryService {
    Category getCategoryById(Long id);
    Category getCategoryByName(String name);
    List<Category> getAllCategories();
    void deleteCategoryById(Long id);
    Category addCategory(Category category);
    Category updateCategory(Category category, Long id);
}
