package com.getir.readingIsGood.controller;

import com.getir.readingIsGood.model.request.OrderRequest;
import com.getir.readingIsGood.model.response.OrderResponse;
import com.getir.readingIsGood.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @PostMapping("/create-new-order")
    public ResponseEntity<OrderResponse> createNewOrder(@RequestBody OrderRequest orderRequest) {
        log.info("Create new order request was received.");

        Optional<OrderResponse> newOrder = orderService.createNewOrder(orderRequest);

        return newOrder.map(orderResponse -> new ResponseEntity<>(orderResponse, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/get-order")
    public ResponseEntity<OrderResponse> getOrder(Long id) {
        Optional<OrderResponse> order = orderService.getOrder(id);

        return order.map(orderResponse -> new ResponseEntity<>(orderResponse, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/get-orders-date-interval")
    public ResponseEntity<List<OrderResponse>> getOrdersDateInterval
            (@RequestParam LocalDateTime startDateTime, @RequestParam LocalDateTime endDateTime) {
        Optional<List<OrderResponse>> ordersDateInterval = orderService.getOrdersDateInterval(startDateTime, endDateTime);

        return ordersDateInterval.map(orderResponses -> new ResponseEntity<>(orderResponses, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/get-all-orders-by-customer")
    public ResponseEntity<List<OrderResponse>> getAllOrdersOfTheCustomer(@RequestParam Long customerId) {
        Optional<List<OrderResponse>> allOrdersOfTheCustomer = orderService.getAllOrdersOfTheCustomer(customerId);

        return allOrdersOfTheCustomer.map(orderResponses -> new ResponseEntity<>(orderResponses, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

}
