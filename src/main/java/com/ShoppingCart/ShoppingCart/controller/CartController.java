package com.ShoppingCart.ShoppingCart.controller;

import com.ShoppingCart.ShoppingCart.dto.CartDto;
import com.ShoppingCart.ShoppingCart.entity.Cart;
import com.ShoppingCart.ShoppingCart.exceptions.ResourceNotFoundException;
import com.ShoppingCart.ShoppingCart.response.ApiResponse;
import com.ShoppingCart.ShoppingCart.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/carts")
public class CartController {
    private final ICartService cartService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/cart/{cartId}")
    public ResponseEntity<ApiResponse> getCartById(@PathVariable Long cartId){
       try {
           Cart cart = cartService.getCart(cartId);
//           CartDto cartDto = cartService.convertCartToDto(cart);
           return ResponseEntity.ok(new ApiResponse("Cart Found" , cart));
       } catch (ResourceNotFoundException e){
           return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
       }
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/clear/{cartId}")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId){
      try {
          cartService.clearCart(cartId);
          return ResponseEntity.ok(new ApiResponse("Clear Cart Success", null));
      } catch (ResourceNotFoundException e){
          return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
      }
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/totalPrice/{cartId}")
    public ResponseEntity<ApiResponse> getTotalPrice(@PathVariable Long cartId){
        try {
            BigDecimal price = cartService.getTotalPrice(cartId);
            return ResponseEntity.ok().body(new ApiResponse("Total Price : " , price));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
}
