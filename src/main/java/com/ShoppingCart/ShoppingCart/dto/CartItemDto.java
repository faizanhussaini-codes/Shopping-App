package com.ShoppingCart.ShoppingCart.dto;

import com.ShoppingCart.ShoppingCart.entity.Product;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class CartItemDto {
 private Long itemId;
 private Integer quantity;
 private BigDecimal unitPrice;
 private ProductDto product;
}
