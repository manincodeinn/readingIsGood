package com.getir.readingIsGood.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderRequestModel {

    private Long customerId;
    private List<BookOrder> bookInfo;

}
