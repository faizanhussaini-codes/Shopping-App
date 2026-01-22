package com.ShoppingCart.ShoppingCart.service.user;

import com.ShoppingCart.ShoppingCart.dto.UserDto;
import com.ShoppingCart.ShoppingCart.entity.User;
import com.ShoppingCart.ShoppingCart.request.CreateUserRequest;
import com.ShoppingCart.ShoppingCart.request.UserUpdateRequest;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request, Long userId);
    void deleteUser(Long userId);

    UserDto convertToUserDto(User user);
}
