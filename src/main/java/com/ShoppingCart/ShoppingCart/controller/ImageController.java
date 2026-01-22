package com.ShoppingCart.ShoppingCart.controller;

import com.ShoppingCart.ShoppingCart.dto.ImageDto;
import com.ShoppingCart.ShoppingCart.entity.Image;
import com.ShoppingCart.ShoppingCart.exceptions.ResourceNotFoundException;
import com.ShoppingCart.ShoppingCart.response.ApiResponse;
import com.ShoppingCart.ShoppingCart.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/images")
public class ImageController {
   private final IImageService imageService;

   @PreAuthorize("hasRole('ADMIN')")
   @PostMapping("/upload")
   public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files,@RequestParam Long productId){
       try {
           List<ImageDto> imageDtos = imageService.saveImages(files,productId);
           return ResponseEntity.ok(new ApiResponse("Upload Success", imageDtos));
       } catch (Exception e) {
           return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Upload Failed" , e.getMessage()));
       }
   }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/image/download/{imageId}")
   public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {
       Image image = imageService.getImageById(imageId);
       ByteArrayResource resource = new
               ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));
       return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
               .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + " \"")
               .body(resource);
   }

   @PreAuthorize("hasRole('ADMIN')")
   @PutMapping("/image/{imageId}/update")
   public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId, @RequestParam  MultipartFile file){
      try {
          Image image = imageService.getImageById(imageId);
          if (image != null){
              imageService.updateImage(file,imageId);
              return ResponseEntity.ok(new ApiResponse("Updated SuccessFully", image));
          }
      }catch (ResourceNotFoundException e){
          return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
      }
    return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Upload Failed" , INTERNAL_SERVER_ERROR ));
   }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/image/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId){
        try {
            Image image = imageService.getImageById(imageId);
            if (image != null){
                imageService.deleteImageById(imageId);
                return ResponseEntity.ok(new ApiResponse("Deleted SuccessFully",null));
            }
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Delete Failed" , INTERNAL_SERVER_ERROR ));
    }
}
