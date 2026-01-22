package com.ShoppingCart.ShoppingCart.controller;

import com.ShoppingCart.ShoppingCart.dto.ProductDto;
import com.ShoppingCart.ShoppingCart.entity.Product;
import com.ShoppingCart.ShoppingCart.exceptions.AlreadyExistException;
import com.ShoppingCart.ShoppingCart.exceptions.ResourceNotFoundException;
import com.ShoppingCart.ShoppingCart.request.AddProductRequest;
import com.ShoppingCart.ShoppingCart.request.ProductUpdateRequest;
import com.ShoppingCart.ShoppingCart.response.ApiResponse;
import com.ShoppingCart.ShoppingCart.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest request){
        try {
            Product product = productService.addProduct(request);
            ProductDto productDto = productService.convertToDto(product);

            return ResponseEntity.ok(new ApiResponse("Product Added" , productDto));
        }catch (AlreadyExistException e){
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts(){
       try {
           List<Product> products = productService.getAllProducts();
           List<ProductDto> convertedProducts = productService.getConvertedProduct(products);
           return ResponseEntity.ok(new ApiResponse("List of Products" , convertedProducts));
       }catch (ResourceNotFoundException e){
           return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("List Not Found", INTERNAL_SERVER_ERROR));
       }
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/product/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id){
       try {
           Product product = productService.getProductById(id);
           ProductDto productDto = productService.convertToDto(product);
           return ResponseEntity.ok(new ApiResponse("Product Found" , productDto));
       }catch (ResourceNotFoundException e){
           return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product Not Found", null));
       }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("product/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest request, @PathVariable Long productId){
        try {
            Product product = productService.updateProduct(request, productId);
            ProductDto productDto = productService.convertToDto(product);

            return ResponseEntity.ok(new ApiResponse("Product Updated", productDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("product/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id){
        try {
            productService.deleteProductById(id);
            return ResponseEntity.ok(new ApiResponse("Deleted Successfully", null));
        } catch (ResourceNotFoundException e) {
           return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/search/by-brand-product")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam String brandName, @RequestParam String name) {
        try {
            List<Product> products = productService.getProductByBrandAndName(brandName, name);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND)
                        .body(new ApiResponse("No Product Found", null));
            }
            List<ProductDto> convertedProducts = productService.getConvertedProduct(products);

            return ResponseEntity.ok(new ApiResponse("Products Found", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/search/by-category-brand")
    public ResponseEntity<ApiResponse> getProductByCategoryAndBrand(@RequestParam String categoryName, @RequestParam String brandName) {
        try {
            List<Product> products =
                    productService.getProductByCategoryAndBrand(categoryName, brandName);

            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND)
                        .body(new ApiResponse("No Product Found", null));
            }
            List<ProductDto> convertedProducts = productService.getConvertedProduct(products);

            return ResponseEntity.ok(new ApiResponse("Products Found", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/search/by-name")
    public ResponseEntity<ApiResponse> getProductByName(@RequestParam String name) {
        try {
            List<Product> products = productService.getProductByName(name);

            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND)
                        .body(new ApiResponse("No Product Found", null));
            }
            List<ProductDto> convertedProducts = productService.getConvertedProduct(products);

            return ResponseEntity.ok(new ApiResponse("Products Found", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }


    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/search/by-brand")
    public ResponseEntity<ApiResponse> getProductByBrand(@RequestParam String brandName) {
        try {
            List<Product> products = productService.getProductByBrand(brandName);

            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND)
                        .body(new ApiResponse("No Product Found", null));
            }
            List<ProductDto> convertedProducts = productService.getConvertedProduct(products);

            return ResponseEntity.ok(new ApiResponse("Products Found", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/search/by-category")
    public ResponseEntity<ApiResponse> getProductByCategory(
            @RequestParam String categoryName) {
        try {
            List<Product> products = productService.getAllProductsByCategory(categoryName);

            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND)
                        .body(new ApiResponse("No Product Found", null));
            }
            List<ProductDto> convertedProducts = productService.getConvertedProduct(products);

            return ResponseEntity.ok(new ApiResponse("Products Found", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/count")
    public ResponseEntity<ApiResponse> countProductByBrandAndName(@RequestParam String brandName, @RequestParam String productName) {
        try {
            var count =
                    productService.countProductByBrandAndName(brandName, productName);
            return ResponseEntity.ok(new ApiResponse("Product Count", count));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

}
