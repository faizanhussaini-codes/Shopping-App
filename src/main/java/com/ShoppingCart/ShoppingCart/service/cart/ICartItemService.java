package com.ShoppingCart.ShoppingCart.service.cart;

import com.ShoppingCart.ShoppingCart.dto.CartItemDto;
import com.ShoppingCart.ShoppingCart.entity.CartItem;

public interface ICartItemService {
    void addItemToCart(Long cartId, Long productId, int quantity);
    void removeItemFromCart(Long cartId, Long productId);
    void updateItemQuantity(Long cartId, Long productId, int quantity);

    CartItem getCartItem(Long cartId, Long productId);

    //check
}
