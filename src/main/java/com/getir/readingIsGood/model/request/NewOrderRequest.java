package com.getir.readingIsGood.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class NewOrderRequest {

    private Long customerId;
    private List<BookOrder> bookInfo;

}
