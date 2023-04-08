package com.getir.readingIsGood.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookOrder {

    private Long bookId;
    private Integer orderCount;

}
