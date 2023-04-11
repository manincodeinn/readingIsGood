package com.getir.readingIsGood.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BookRequest {

    @Size(min = 2, message = "Book name should have least 2 characters.")
    @NotNull
    private String name;
    @Size(min = 5, message = "Author name should have least 5 characters.")
    @NotNull
    private String author;
    @PositiveOrZero
    @NotNull
    private Double price;
    @PositiveOrZero
    @NotNull
    private Integer stockCount;

}
