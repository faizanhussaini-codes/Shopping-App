package com.ShoppingCart.ShoppingCart.service.cart;

import com.ShoppingCart.ShoppingCart.dto.CartDto;
import com.ShoppingCart.ShoppingCart.entity.Cart;
import com.ShoppingCart.ShoppingCart.entity.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);

    Cart initializeNewCart(User user);

    Cart getCartByUserId(Long userId);

//    CartDto convertCartToDto(Cart cart);
}
