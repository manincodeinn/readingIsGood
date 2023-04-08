package com.getir.readingIsGood.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@Entity(name = "OrderDetails")
public class Order {

    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    private Customer customer;
    @OneToMany
    private List<Book> books;
    private Double totalPrice;
    @CreationTimestamp
    private LocalDateTime orderDate;

}
