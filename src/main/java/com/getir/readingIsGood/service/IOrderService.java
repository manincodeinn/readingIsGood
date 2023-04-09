package com.getir.readingIsGood.service;

import com.getir.readingIsGood.entity.Order;
import com.getir.readingIsGood.model.request.OrderRequest;
import com.getir.readingIsGood.model.response.OrderResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface IOrderService {

    OrderResponse createNewOrder(OrderRequest orderRequest);

    OrderResponse getOrder(Long id);

    List<OrderResponse> getOrdersDateInterval(LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<OrderResponse> getAllOrdersOfTheCustomer(Long id);

}
