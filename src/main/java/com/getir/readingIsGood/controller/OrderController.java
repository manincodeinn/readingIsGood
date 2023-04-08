package com.getir.readingIsGood.controller;

import com.getir.readingIsGood.entity.Order;
import com.getir.readingIsGood.model.OrderRequestModel;
import com.getir.readingIsGood.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @PostMapping("/create-new-order")
    public ResponseEntity<Order> createNewOrder(@RequestBody OrderRequestModel orderRequestModel) {
        return new ResponseEntity<>(orderService.createNewOrder(orderRequestModel), HttpStatus.OK);
    }

}
