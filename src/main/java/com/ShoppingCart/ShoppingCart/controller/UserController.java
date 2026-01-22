package com.ShoppingCart.ShoppingCart.controller;

import com.ShoppingCart.ShoppingCart.dto.CartDto;
import com.ShoppingCart.ShoppingCart.dto.UserDto;
import com.ShoppingCart.ShoppingCart.entity.User;
import com.ShoppingCart.ShoppingCart.exceptions.AlreadyExistException;
import com.ShoppingCart.ShoppingCart.exceptions.ResourceNotFoundException;
import com.ShoppingCart.ShoppingCart.request.CreateUserRequest;
import com.ShoppingCart.ShoppingCart.request.UserUpdateRequest;
import com.ShoppingCart.ShoppingCart.response.ApiResponse;
import com.ShoppingCart.ShoppingCart.service.cart.ICartService;
import com.ShoppingCart.ShoppingCart.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final IUserService userService;
    private final ICartService cartService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-user/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId){
        try {
            User user = userService.getUserById(userId);

            UserDto userDto = userService.convertToUserDto(user);
//            if (user.getCart() != null){
//                CartDto cartDto = cartService.convertCartToDto(user.getCart());
//                userDto.setCartDto(cartDto);
//            }
            return ResponseEntity.ok().body(new ApiResponse("User Found" , userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage() , null));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create-user")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request) {
        try {
            User user = userService.createUser(request);
            UserDto userDto = userService.convertToUserDto(user);
            return ResponseEntity.ok().body(new ApiResponse("User Created Successfully", userDto));
        } catch (AlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update-user/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UserUpdateRequest request, @PathVariable Long userId){
        try {
            User user = userService.updateUser(request, userId);
            UserDto userDto = userService.convertToUserDto(user);
            return ResponseEntity.ok().body(new ApiResponse("User Updated Successfully" , userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse( e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete-user/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId){
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok().body(new ApiResponse("Delete Success" , null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse( e.getMessage(), null));
        }
    }



}
