package ru.merezh.orderservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "order_items")
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    public OrderItem(String namePosition, String idPosition, BigDecimal amount, int quantity) {
        this.namePosition = namePosition;
        this.idPosition = idPosition;
        this.amount = amount;
        this.quantity = quantity;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private String namePosition;
    private String idPosition;
    private BigDecimal amount;
    private int quantity;
}
