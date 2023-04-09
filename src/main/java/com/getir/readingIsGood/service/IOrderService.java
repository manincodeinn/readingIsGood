package com.getir.readingIsGood.service;

import com.getir.readingIsGood.entity.Order;
import com.getir.readingIsGood.model.request.NewOrderRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface IOrderService {

    Order createNewOrder(NewOrderRequest newOrderRequest);

    Order getOrder(Long id);

    List<Order> getOrdersDateInterval(LocalDateTime startDateTime, LocalDateTime endDateTime);

}
