package com.getir.readingIsGood.controller;

import com.getir.readingIsGood.entity.Order;
import com.getir.readingIsGood.model.OrderRequestModel;
import com.getir.readingIsGood.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @PostMapping("/create-new-order")
    public ResponseEntity<Order> createNewOrder(@RequestBody OrderRequestModel orderRequestModel) {
        return new ResponseEntity<>(orderService.createNewOrder(orderRequestModel), HttpStatus.OK);
    }

    @GetMapping("/get-order")
    public ResponseEntity<Order> getOrder(Long id) {
        return new ResponseEntity<>(orderService.getOrder(id), HttpStatus.OK);
    }

    @GetMapping("/get-orders-date-interval")
    public ResponseEntity<List<Order>> getOrdersDateInterval
            (@RequestParam LocalDateTime startDateTime, @RequestParam LocalDateTime endDateTime) {
        return new ResponseEntity<>(orderService.getOrdersDateInterval(startDateTime, endDateTime), HttpStatus.OK);
    }

}
