package com.ShoppingCart.ShoppingCart.dto;

import com.ShoppingCart.ShoppingCart.entity.OrderItem;
import com.ShoppingCart.ShoppingCart.enums.OrderStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class OrderDto {
    private Long orderId;
    private LocalDate orderDate;
    private BigDecimal totalAmount;
    private String Status;
    private Set<OrderItemDto> orderItems;
}
