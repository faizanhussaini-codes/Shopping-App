package com.ShoppingCart.ShoppingCart.service.product;

import com.ShoppingCart.ShoppingCart.dto.ImageDto;
import com.ShoppingCart.ShoppingCart.dto.ProductDto;
import com.ShoppingCart.ShoppingCart.entity.Category;
import com.ShoppingCart.ShoppingCart.entity.Image;
import com.ShoppingCart.ShoppingCart.entity.Product;
import com.ShoppingCart.ShoppingCart.exceptions.AlreadyExistException;
import com.ShoppingCart.ShoppingCart.exceptions.ResourceNotFoundException;
import com.ShoppingCart.ShoppingCart.repo.CategoryRepository;
import com.ShoppingCart.ShoppingCart.repo.ImageRepository;
import com.ShoppingCart.ShoppingCart.repo.ProductRepository;
import com.ShoppingCart.ShoppingCart.request.AddProductRequest;
import com.ShoppingCart.ShoppingCart.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{

    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public Product addProduct(AddProductRequest request) {
        //Check if the category is found in the DB
        //If yes et it as the new product category
        //if no then save it as a new category
        //then set as the new product category

//        If object creation depends on a DB lookup →
//        Use orElseGet() or a clean if–else, but always assign back to the same variable.
//        Optional.orElseGet() is preferred because it cleanly handles “find or create” logic without duplication or null risks.

        if (productExists(request.getName(), request.getBrand())){
            throw new AlreadyExistException(request.getBrand()+ " " + request.getName() + " Already Exists ! Update Product Instead");
        }

        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(()-> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return productRepository.save(createProduct(request,category));
    }

    private boolean productExists(String name, String brand){
        return productRepository.existsByNameAndBrand(name, brand);
    }

    private Product createProduct(AddProductRequest request, Category category){
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Product Not Found!"));
    }

    @Override
    public void deleteProductById(Long id) {
    if (!productRepository.existsById(id)){
       throw new ResourceNotFoundException("Product Does Not exist");
    }
        productRepository.deleteById(id);
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId){
        return productRepository.findById(productId)
                .map(existingProduct -> updateExisitngProduct(existingProduct, request))
                .map(productRepository::save)
                .orElseThrow(()-> new ResourceNotFoundException("Product Not Found!"));
    }
    //    public Product updateProduct(ProductUpdateRequest request, Long productId) {
//        Product existing = productRepository.findById(productId)
//                .orElseThrow(() -> new ProductNotFoundException("Product Not Found"));
//            return productRepository.save(updateExisitngProduct(existing, request));
//    }

    private Product updateExisitngProduct(Product exisitngProduct, ProductUpdateRequest request){
        exisitngProduct.setName(request.getName());
        exisitngProduct.setBrand(request.getBrand());
        exisitngProduct.setPrice(request.getPrice());
        exisitngProduct.setInventory(request.getInventory());
        exisitngProduct.setDescription(request.getDescription());
        Category category = categoryRepository.findByName(request.getCategory().getName());
        exisitngProduct.setCategory(category);
        return exisitngProduct;
    }


    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getAllProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductByCategoryAndBrand( String category , String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    @Override
    public List<ProductDto> getConvertedProduct(List<Product> products){
        return products.stream().map(this::convertToDto).toList();
    }

    @Override
    public ProductDto convertToDto(Product product){
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .toList();
        productDto.setImages(imageDtos);
        return productDto;
    }

}
