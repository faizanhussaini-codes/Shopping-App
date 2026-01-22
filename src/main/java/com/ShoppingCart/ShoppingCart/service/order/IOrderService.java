package com.ShoppingCart.ShoppingCart.service.order;

import com.ShoppingCart.ShoppingCart.dto.OrderDto;
import com.ShoppingCart.ShoppingCart.entity.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long userId);

    List<OrderDto> getUserOrder(Long userId);

    OrderDto convertToDto(Order order);
}
