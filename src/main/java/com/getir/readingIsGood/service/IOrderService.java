package com.getir.readingIsGood.service;

import com.getir.readingIsGood.entity.Order;
import com.getir.readingIsGood.model.OrderRequestModel;

import java.time.LocalDateTime;
import java.util.List;

public interface IOrderService {

    Order createNewOrder(OrderRequestModel orderRequestModel);

    Order getOrder(Long id);

    List<Order> getOrdersDateInterval(LocalDateTime startDateTime, LocalDateTime endDateTime);

}
