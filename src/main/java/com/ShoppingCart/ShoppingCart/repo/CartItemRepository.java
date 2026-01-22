package com.ShoppingCart.ShoppingCart.repo;

import com.ShoppingCart.ShoppingCart.entity.Cart;
import com.ShoppingCart.ShoppingCart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteAllByCartId(Long id);

    Cart findByCartId(Long cartId);
}
