package com.getir.readingIsGood.controller;

import com.getir.readingIsGood.model.request.OrderRequest;
import com.getir.readingIsGood.model.response.OrderResponse;
import com.getir.readingIsGood.service.IOrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/order")
@SecurityRequirement(name = "basicAuth")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @PostMapping("/create-new-order")
    public ResponseEntity<OrderResponse> createNewOrder(@Valid @RequestBody OrderRequest orderRequest) {
        log.info("Create new order request was received.");

        Optional<OrderResponse> newOrder = orderService.createNewOrder(orderRequest);

        return newOrder.map(orderResponse -> new ResponseEntity<>(orderResponse, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/get-order")
    public ResponseEntity<OrderResponse> getOrder(@RequestParam @NotNull Long id) {
        Optional<OrderResponse> order = orderService.getOrder(id);

        return order.map(orderResponse -> new ResponseEntity<>(orderResponse, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/get-orders-date-interval")
    public ResponseEntity<List<OrderResponse>> getOrdersDateInterval(@RequestParam @NotNull LocalDateTime startDateTime,
                                                                     @RequestParam @NotNull LocalDateTime endDateTime,
                                                                     @PageableDefault(size = 15) Pageable pageable) {
        Optional<List<OrderResponse>> ordersDateInterval =
                orderService.getOrdersDateInterval(startDateTime, endDateTime, pageable);

        return ordersDateInterval.map(orderResponses -> new ResponseEntity<>(orderResponses, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/get-all-orders-by-customer")
    public ResponseEntity<List<OrderResponse>> getAllOrdersOfTheCustomer(@RequestParam @NotNull Long customerId,
                                                                         @PageableDefault(size = 15) Pageable pageable) {
        Optional<List<OrderResponse>> allOrdersOfTheCustomer =
                orderService.getAllOrdersOfTheCustomer(customerId, pageable);

        return allOrdersOfTheCustomer.map(orderResponses -> new ResponseEntity<>(orderResponses, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

}
