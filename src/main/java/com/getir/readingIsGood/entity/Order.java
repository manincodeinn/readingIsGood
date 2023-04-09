package com.getir.readingIsGood.entity;

import com.getir.readingIsGood.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "OrderDetails")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Customer customer;
    @ElementCollection
    private Map<Long, Integer> bookOrder;
    private Double totalPrice;
    @CreationTimestamp
    private LocalDateTime orderDate;
    private OrderStatus status;

}
