package com.getir.readingIsGood.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookResponse {

    private String name;
    private String author;
    private Double price;
    private Integer stockCount;

}
