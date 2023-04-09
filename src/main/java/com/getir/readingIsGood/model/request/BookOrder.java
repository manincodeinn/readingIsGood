package com.getir.readingIsGood.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookOrder {

    private Long bookId;
    private Integer orderCount;

}
