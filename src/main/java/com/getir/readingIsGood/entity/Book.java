package com.getir.readingIsGood.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class Book {

    @Id
    @GeneratedValue
    private Long id;
    @Size(min = 2, message = "Book name should have least 2 characters.")
    private String name;
    @Size(min = 5, message = "Author name should have least 5 characters.")
    private String author;
    @PositiveOrZero
    private Double price;
    @PositiveOrZero
    private Integer stockCount;

}
