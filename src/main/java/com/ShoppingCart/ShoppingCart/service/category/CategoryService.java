package com.ShoppingCart.ShoppingCart.service.category;

import com.ShoppingCart.ShoppingCart.entity.Category;
import com.ShoppingCart.ShoppingCart.exceptions.AlreadyExistException;
import com.ShoppingCart.ShoppingCart.exceptions.ResourceNotFoundException;
import com.ShoppingCart.ShoppingCart.repo.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
    private final CategoryRepository categoryRepository;



    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Category Not Found"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
//
//    Mental rule to remember forever
//
//    method() → executes now
//
//            () -> method() → executes later
//
//    Class::method → reference, executes later
//
//    ifPresentOrElse needs executes later.
    @Override
    public void deleteCategoryById(Long id) {           //class name:: method
        categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete, ()-> {
            throw new ResourceNotFoundException("Category Not Found");
        });
    }

    @Override
    public Category addCategory(Category category){
        return Optional.of(category).filter(c -> !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository::save)
                .orElseThrow(()-> new AlreadyExistException(category.getName()+ " already exists"));
    }

    @Override
    public Category updateCategory(Category category,Long id) {
        return Optional.ofNullable(getCategoryById(id)).map(oldCategory -> {
        oldCategory.setName(category.getName());
        return categoryRepository.save(oldCategory);
        }).orElseThrow(()-> new ResourceNotFoundException("Category Not Found!"));}}

