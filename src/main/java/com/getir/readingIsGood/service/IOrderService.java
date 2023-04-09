package com.getir.readingIsGood.service;

import com.getir.readingIsGood.model.request.OrderRequest;
import com.getir.readingIsGood.model.response.OrderResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IOrderService {

    Optional<OrderResponse> createNewOrder(OrderRequest orderRequest);

    Optional<OrderResponse> getOrder(Long id);

    Optional<List<OrderResponse>> getOrdersDateInterval(LocalDateTime startDateTime, LocalDateTime endDateTime);

    Optional<List<OrderResponse>> getAllOrdersOfTheCustomer(Long id);

}
