package com.foodcloud.order.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "orders") // "order" — зарезервированное слово в SQL, поэтому переименовываем таблицу
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long restaurantId;

    @NotBlank
    private String customerName;

    @NotBlank
    private String deliveryAddress;

    @Enumerated(EnumType.STRING) // сохраняет в БД строку "NEW", а не число 0
    private OrderStatus status;

    private BigDecimal totalPrice;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;
}
