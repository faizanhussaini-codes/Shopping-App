package com.ShoppingCart.ShoppingCart.service.cart;

import com.ShoppingCart.ShoppingCart.dto.CartDto;
import com.ShoppingCart.ShoppingCart.dto.CartItemDto;
import com.ShoppingCart.ShoppingCart.entity.Cart;
import com.ShoppingCart.ShoppingCart.entity.CartItem;
import com.ShoppingCart.ShoppingCart.entity.User;
import com.ShoppingCart.ShoppingCart.exceptions.ResourceNotFoundException;
import com.ShoppingCart.ShoppingCart.repo.CartItemRepository;
import com.ShoppingCart.ShoppingCart.repo.CartRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AtomicLong cartIdGenerator = new AtomicLong(0);
    private final ModelMapper modelMapper;


    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Cart Not Found"));
        BigDecimal totalAmount = cart.getTotalAmount();//No need
        cart.setTotalAmount(totalAmount);//No need
        return cartRepository.save(cart);//No need
    }

    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cart.getItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }

    // Earlier we used this to initalize the cart as we were not having user
    //    @Override
    //    public Long initializeNewCart(){
    //        Cart newCart = new Cart();
    //        return cartRepository.save(newCart).getId();
    //    }

    @Override
    public Cart initializeNewCart(User user){
        return Optional.ofNullable(getCartByUserId(user.getId()))
                .orElseGet(()->{
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }



    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

//    @Override
//    public CartDto convertCartToDto(Cart cart){
//        return modelMapper.map(cart, CartDto.class);
//    }
}
