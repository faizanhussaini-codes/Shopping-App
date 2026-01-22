package com.ShoppingCart.ShoppingCart.service.product;

import com.ShoppingCart.ShoppingCart.dto.ProductDto;
import com.ShoppingCart.ShoppingCart.entity.Product;
import com.ShoppingCart.ShoppingCart.request.AddProductRequest;
import com.ShoppingCart.ShoppingCart.request.ProductUpdateRequest;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest product);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(ProductUpdateRequest product, Long productId);
    List<Product> getAllProducts();
    List<Product> getAllProductsByCategory(String category);
    List<Product> getProductByBrand(String brand);
    List<Product> getProductByCategoryAndBrand(String brand, String category);
    List<Product> getProductByName(String name);
    List<Product> getProductByBrandAndName(String brand, String name);
    Long countProductByBrandAndName(String brand, String name);

    List<ProductDto> getConvertedProduct(List<Product> products);

    ProductDto convertToDto(Product product);
}
