package com.ShoppingCart.ShoppingCart.repo;

import com.ShoppingCart.ShoppingCart.entity.Order;
import com.ShoppingCart.ShoppingCart.service.order.OrderService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}
