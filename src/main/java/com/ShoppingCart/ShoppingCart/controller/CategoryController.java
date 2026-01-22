package com.ShoppingCart.ShoppingCart.controller;

import com.ShoppingCart.ShoppingCart.entity.Category;
import com.ShoppingCart.ShoppingCart.exceptions.AlreadyExistException;
import com.ShoppingCart.ShoppingCart.exceptions.ResourceNotFoundException;
import com.ShoppingCart.ShoppingCart.response.ApiResponse;
import com.ShoppingCart.ShoppingCart.service.category.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
public class CategoryController {

    private final ICategoryService categoryService;
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/category/{id}/category")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id){
        try {
            Category category = categoryService.getCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Category Found " , category));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse>getAllCategories() {
       try {
           List<Category> categories = categoryService.getAllCategories();
           return ResponseEntity.ok(new ApiResponse("Here is the List : " , categories));
       } catch (Exception e) { // why static import
           return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error : " , INTERNAL_SERVER_ERROR));
       }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category){
       try {
           Category category1 = categoryService.addCategory(category);
           return ResponseEntity.ok(new ApiResponse("Category Added " , category1));
       }catch (AlreadyExistException e){
           return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(),null));
       }
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("{name}/category")
    public ResponseEntity<ApiResponse> getCategoryByName(@RequestBody String name){
        try {
            Category category = categoryService.getCategoryByName(name);
            return ResponseEntity.ok(new ApiResponse("Category Found " , category));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Category Not Found " , null));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/category/{id}/category")
    public ResponseEntity<ApiResponse> deleteCategoryById(@PathVariable Long id){
        try {
             categoryService.deleteCategoryById(id);
             return ResponseEntity.ok(new ApiResponse("Deleted Successfully" , null));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Category Not Found " , null));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("category/update/{id}")
    public ResponseEntity<ApiResponse> updateCategory(@RequestBody Category category, @PathVariable Long id){
       try {
           Category updatedCategory = categoryService.updateCategory(category, id);
           return ResponseEntity.ok(new ApiResponse("Category updated" , updatedCategory));
       }catch (ResourceNotFoundException e){
           return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Category Not Found", null));
       }
    }

}
