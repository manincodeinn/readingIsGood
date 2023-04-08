package com.getir.readingIsGood.service;

import com.getir.readingIsGood.entity.Order;
import com.getir.readingIsGood.model.OrderRequestModel;

public interface IOrderService {

    Order createNewOrder(OrderRequestModel orderRequestModel);

}
