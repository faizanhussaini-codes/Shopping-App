package com.ShoppingCart.ShoppingCart.service.image;

import com.ShoppingCart.ShoppingCart.dto.ImageDto;
import com.ShoppingCart.ShoppingCart.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImages(List<MultipartFile> files, Long productId);
    void updateImage(MultipartFile file, Long imageId);
}
