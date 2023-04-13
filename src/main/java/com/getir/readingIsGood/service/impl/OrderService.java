package com.getir.readingIsGood.service.impl;

import com.getir.readingIsGood.entity.Book;
import com.getir.readingIsGood.entity.Customer;
import com.getir.readingIsGood.entity.Order;
import com.getir.readingIsGood.model.enums.OrderStatus;
import com.getir.readingIsGood.model.exception.ReadingIsGoodException;
import com.getir.readingIsGood.model.request.BookOrder;
import com.getir.readingIsGood.model.request.OrderRequest;
import com.getir.readingIsGood.model.response.OrderResponse;
import com.getir.readingIsGood.repository.IOrderRepository;
import com.getir.readingIsGood.service.IBookService;
import com.getir.readingIsGood.service.ICustomerService;
import com.getir.readingIsGood.service.IOrderService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService implements IOrderService {

    @Autowired
    private IOrderRepository orderRepository;
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private IBookService bookService;

    @Transactional
    @Override
    public Optional<OrderResponse> createNewOrder(OrderRequest orderRequest) {
        Order order;

        try {
            Optional<Customer> customer = customerService.getCustomerWithId(orderRequest.getCustomerId());

            if (customer.isEmpty()) {
                log.warn("There is no customer with id: {}", orderRequest.getCustomerId());
                return Optional.empty();
            }

            List<Optional<Book>> bookList = orderRequest.getBookInfo().stream()
                    .filter(bookOrder -> bookService.isBookExistAndStockEnough(bookOrder.getBookId(), bookOrder.getOrderCount()))
                    .map(bookOrder -> bookService.getBookWithId(bookOrder.getBookId()))
                    .toList();

            boolean allBookValid = bookList.stream().anyMatch(Optional::isPresent);

            if (bookList.size() == 0 || !allBookValid) {
                log.warn("There is no valid book in order.");
                return Optional.empty();
            }

            Map<Long, Integer> bookAndOrderCountMap = orderRequest.getBookInfo().stream()
                    .collect(Collectors.toMap(bookOrder -> bookOrder.getBookId(), bookOrder -> bookOrder.getOrderCount()));

            Double totalPrice = bookList.stream()
                    .map(book -> book.get().getPrice() * bookAndOrderCountMap.get(book.get().getId()))
                    .collect(Collectors.summingDouble(Double::doubleValue));

            bookList.forEach(book -> bookService.updateStockCount(book.get().getId(),
                    book.get().getStockCount() - bookAndOrderCountMap.get(book.get().getId())));

            order = Order.builder()
                    .customer(customer.get())
                    .orderDate(LocalDateTime.now())
                    .totalPrice(totalPrice)
                    .bookOrder(bookAndOrderCountMap)
                    .status(OrderStatus.RECEIVED)
                    .build();

            Order result = orderRepository.save(order);

            log.info("New order was created. {}", result);
        } catch (Exception exception) {
            throw new ReadingIsGoodException("Error occurred while creating new order.", exception);
        }

        return Optional.ofNullable(OrderResponse.builder()
                .customerId(order.getCustomer().getId())
                .books(orderRequest.getBookInfo())
                .totalPrice(order.getTotalPrice())
                .orderDateTime(order.getOrderDate())
                .status(order.getStatus())
                .build());
    }

    @Override
    public Optional<OrderResponse> getOrder(Long id) {
        Optional<Order> order;
        List<BookOrder> bookOrders;

        try {
            order = orderRepository.findById(id);

            if (!order.isPresent()) {
                log.warn("There is no order with id: {}", id);
                return Optional.empty();
            }

            bookOrders = order.get().getBookOrder().entrySet().stream()
                    .map(entry -> new BookOrder(entry.getKey(), entry.getValue())).toList();
        } catch (Exception exception) {
            throw new ReadingIsGoodException("Error occurred while getting order.", exception);
        }

        return Optional.ofNullable(OrderResponse.builder()
                .customerId(order.get().getCustomer().getId())
                .books(bookOrders)
                .totalPrice(order.get().getTotalPrice())
                .orderDateTime(order.get().getOrderDate())
                .status(order.get().getStatus())
                .build());
    }

    @Override
    public Optional<List<OrderResponse>> getOrdersDateInterval(LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable) {
        if(startDateTime.isAfter(endDateTime)) {
            log.warn("End date cannot be older than start date.");
            return Optional.empty();
        }

        List<Order> orderList;

        try {
            orderList = orderRepository.findByOrderDateBetween(startDateTime, endDateTime, pageable);

            if (orderList == null || orderList.size() == 0) {
                log.warn("There is no order between {}-{}", startDateTime, endDateTime);
                return Optional.empty();
            }
        } catch (Exception exception) {
            throw new ReadingIsGoodException("Error occurred while getting order date interval.", exception);
        }

        return Optional.of(orderList.stream().map(order -> OrderResponse.builder()
                .customerId(order.getCustomer().getId())
                .books(OrderResponse.generateBookOrderListFromMap(order.getBookOrder()))
                .totalPrice(order.getTotalPrice())
                .orderDateTime(order.getOrderDate())
                .status(order.getStatus())
                .build()).toList());
    }

    @Override
    public Optional<List<OrderResponse>> getAllOrdersOfTheCustomer(Long id, Pageable pageable) {
        Page<Order> allOrdersOfTheCustomer;

        try {
            Optional<Customer> customer = customerService.getCustomerWithId(id);

            if (customer.isEmpty()) {
                log.warn("There is no customer with id: {}", id);
                return Optional.empty();
            }

            allOrdersOfTheCustomer = orderRepository.findByCustomer(customer.get(), pageable);

            if (allOrdersOfTheCustomer == null || allOrdersOfTheCustomer.getContent().size() == 0) {
                log.warn("There is no order of the customer. Customer: {}", customer);
                return Optional.empty();
            }
        } catch (Exception exception) {
            throw new ReadingIsGoodException("Error occurred while getting all orders of the customer.", exception);
        }

        return Optional.of(allOrdersOfTheCustomer.getContent().stream().map(order -> OrderResponse.builder()
                .customerId(order.getCustomer().getId())
                .books(OrderResponse.generateBookOrderListFromMap(order.getBookOrder()))
                .totalPrice(order.getTotalPrice())
                .orderDateTime(order.getOrderDate())
                .status(order.getStatus())
                .build()).toList());
    }

}
