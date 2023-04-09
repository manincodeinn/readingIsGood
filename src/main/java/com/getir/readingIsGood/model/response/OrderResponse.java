package com.getir.readingIsGood.model.response;

import com.getir.readingIsGood.model.enums.OrderStatus;
import com.getir.readingIsGood.model.request.BookOrder;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class OrderResponse {

    private Long customerId;
    private List<BookOrder> books;
    private Double totalPrice;
    private LocalDateTime orderDateTime;
    private OrderStatus status;

    public static List<BookOrder> generateBookOrderListFromMap(Map<Long, Integer> bookOrderMap) {
        return bookOrderMap.entrySet().stream()
                .map(entry -> new BookOrder(entry.getKey(), entry.getValue())).toList();
    }

}
