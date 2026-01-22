package com.ShoppingCart.ShoppingCart.controller;

import com.ShoppingCart.ShoppingCart.dto.OrderDto;
import com.ShoppingCart.ShoppingCart.entity.Order;
import com.ShoppingCart.ShoppingCart.exceptions.ResourceNotFoundException;
import com.ShoppingCart.ShoppingCart.response.ApiResponse;
import com.ShoppingCart.ShoppingCart.service.order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static ch.qos.logback.core.util.AggregationType.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
public class OrderController {

    private final IOrderService orderService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create-order")
    public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId){
        try {
            Order order = orderService.placeOrder(userId);
            OrderDto orderDto = orderService.convertToDto(order);
            return ResponseEntity.ok(new ApiResponse("Order Placed Successfully" , orderDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error occurred" , e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-order/{orderId}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId){
        try {
            OrderDto order = orderService.getOrder(orderId);
            return ResponseEntity.ok(new ApiResponse("Here's Your Order" , order));
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Oops! " , e.getMessage() ));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-order-list/{userId}")
    public ResponseEntity<ApiResponse> getUserOrder(@PathVariable Long userId){
        try {
            List<OrderDto> orders = orderService.getUserOrder(userId);
            return ResponseEntity.ok(new ApiResponse("List of Your Orders " , orders));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No Orders Found" , e.getMessage()));
        }
    }



}
