package com.foodcloud.order.service;

import com.foodcloud.order.entity.Order;
import com.foodcloud.order.entity.OrderStatus;
import com.foodcloud.order.exception.OrderNotFoundException;
import com.foodcloud.order.exception.RestaurantNotAvailableException;
import com.foodcloud.order.repository.OrderRepository;
import com.foodcloud.order.service.client.RestaurantClient;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final RestaurantClient restaurantClient;

    @Retry(name = "restaurantServiceRetry")
    @CircuitBreaker(name = "restaurantServiceCB", fallbackMethod = "createOrderFallback")
    public Order createOrder(Order order) {
        order.setStatus(OrderStatus.NEW);
        order.setCreatedAt(LocalDateTime.now());

        // устанавливаем обратную ссылку order в каждом item,
        // иначе JPA не заполнит order_id в таблице order_item
        order.getOrderItems().forEach(item -> item.setOrder(order));

        order.setTotalPrice(calculateTotalPrice(order));

        try {
            restaurantClient.getRestaurantById(order.getRestaurantId());
        }  catch (FeignException.NotFound e) {
            // restaurant-service вернул 404 — ресторан не найден
            throw new RestaurantNotAvailableException("Restaurant with id " + order.getRestaurantId() + " not found");
        }

        return orderRepository.save(order);
    }

    public Order createOrderFallback(Order order, Exception e) {
        order.setStatus(OrderStatus.PENDING_VERIFICATION);
        order.setCreatedAt(LocalDateTime.now());
        // устанавливаем обратную ссылку order в каждом item,
        // иначе JPA не заполнит order_id в таблице order_item
        order.getOrderItems().forEach(item -> item.setOrder(order));
        order.setTotalPrice(calculateTotalPrice(order));
        return orderRepository.save(order);
    }


    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order updateOrderStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    private BigDecimal calculateTotalPrice(Order order) {
        return order.getOrderItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
