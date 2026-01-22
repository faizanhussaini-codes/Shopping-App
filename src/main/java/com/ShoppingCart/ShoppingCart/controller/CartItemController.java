package com.ShoppingCart.ShoppingCart.controller;

import com.ShoppingCart.ShoppingCart.entity.Cart;
import com.ShoppingCart.ShoppingCart.entity.User;
import com.ShoppingCart.ShoppingCart.exceptions.ResourceNotFoundException;
import com.ShoppingCart.ShoppingCart.response.ApiResponse;
import com.ShoppingCart.ShoppingCart.service.cart.ICartItemService;
import com.ShoppingCart.ShoppingCart.service.cart.ICartService;
import com.ShoppingCart.ShoppingCart.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {

    private final ICartItemService cartItemService;
    private final ICartService cartService;
    private final IUserService userService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestParam Long userId , @RequestParam Long productId, @RequestParam int quantity){

        try {
                User user = userService.getUserById(userId);// Manually adding userId
                Cart cart=cartService.initializeNewCart(user);//Checking if user has cart if not create if yes add item to cart

            cartItemService.addItemToCart(cart.getId(),productId,quantity);
            return ResponseEntity.ok(new ApiResponse("Item Added" , null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/remove/cartId/{cartId}/productId/{productId}")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId,@PathVariable Long productId){
        try {
            cartItemService.removeItemFromCart(cartId,productId);
            return ResponseEntity.ok(new ApiResponse("Item Removed" , null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update/cartId/{cartId}/itemId/{itemId}/quantity")
    public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId, @PathVariable Long itemId, @RequestParam int quantity){
        try {
            cartItemService.updateItemQuantity(cartId,itemId,quantity);
            return ResponseEntity.ok().body(new ApiResponse("Quantity Updated Successfully" , null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }

    }
}
