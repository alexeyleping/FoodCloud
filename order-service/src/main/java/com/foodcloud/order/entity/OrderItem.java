package com.foodcloud.order.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long menuItemId;

    private String menuItemName;

    @NotNull
    @Positive
    private BigDecimal price;

    @NotNull
    @Positive
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order order;
}
