package com.ShoppingCart.ShoppingCart.service.order;

import com.ShoppingCart.ShoppingCart.dto.OrderDto;
import com.ShoppingCart.ShoppingCart.entity.*;
import com.ShoppingCart.ShoppingCart.enums.OrderStatus;
import com.ShoppingCart.ShoppingCart.exceptions.ResourceNotFoundException;
import com.ShoppingCart.ShoppingCart.repo.OrderRepository;
import com.ShoppingCart.ShoppingCart.repo.ProductRepository;
import com.ShoppingCart.ShoppingCart.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ICartService cartService;
    private final ModelMapper modelMapper;

    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItem(order, cart);
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());
        return savedOrder;
    }


//    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList){
//        return orderItemList.stream()
//                .map(item -> item.getPrice()
//                        .multiply(new BigDecimal(item.getQuantity())))
//                .reduce(BigDecimal.ZERO , BigDecimal::add);
//    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList){
        BigDecimal total = BigDecimal.ZERO;
            for(OrderItem item : orderItemList){
                 BigDecimal values = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                 total = total.add(values);
            }
            return total;
    }

    //TODO: Check the inventory and quantity
//    private List<OrderItem> createOrderItem(Order order, Cart cart){
//        return cart.getItems().stream().map(cartItem -> {
//            Product product = cartItem.getProduct();
//            product.setInventory(product.getInventory() - cartItem.getQuantity());
//            productRepository.save(product);
//            return new OrderItem(
//                    order,
//                    product,
//                    cartItem.getQuantity(),
//                    cartItem.getUnitPrice()
//            );
//        }).toList();
//    }

    //Used in place order
    private List<OrderItem> createOrderItem(Order order, Cart cart){
        List<OrderItem> item = new ArrayList<>();
        for(CartItem items : cart.getItems()) {
           Product product = items.getProduct();
           product.setInventory(product.getInventory() - items.getQuantity());
           productRepository.save(product);
            item.add(new OrderItem(order , product, items.getQuantity(), items.getUnitPrice())) ;
       }
       return item;
    }

    //Used in Place Order
    private Order createOrder(Cart cart){
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

//    @Override
//    public OrderDto getOrder(Long orderId) {
//        return orderRepository.findById(orderId)
//                .map(this::convertToDto)
//                .orElseThrow(()-> new ResourceNotFoundException("Order Not found"));
//    }

    @Override
    public OrderDto getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->(new ResourceNotFoundException("Not Found")));
        return convertToDto(order);
    }

    @Override
    public List<OrderDto> getUserOrder(Long userId){
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(this::convertToDto).toList();
    }

    @Override
    public OrderDto convertToDto(Order order){
        return modelMapper.map(order, OrderDto.class);
    }





}
