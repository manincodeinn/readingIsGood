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
    public OrderResponse createNewOrder(OrderRequest orderRequest) {
        Customer customer = customerService.getCustomerById(orderRequest.getCustomerId());

        List<Book> bookList = orderRequest.getBookInfo().stream()
                .filter(bookOrder -> bookService.isBookExistAndStockEnough(bookOrder.getBookId(), bookOrder.getOrderCount()))
                .map(bookOrder -> bookService.getBookById(bookOrder.getBookId()))
                .toList();

        if (bookList.size() == 0) {
            log.warn("There is no valid book in order.");
            throw new ReadingIsGoodException("There is no valid book in order.");
        }

        Map<Long, Integer> bookAndOrderCountMap = orderRequest.getBookInfo().stream()
                .collect(Collectors.toMap(bookOrder -> bookOrder.getBookId(), bookOrder -> bookOrder.getOrderCount()));

        Double totalPrice = bookList.stream()
                .map(book -> book.getPrice() * bookAndOrderCountMap.get(book.getId()))
                .collect(Collectors.summingDouble(Double::doubleValue));

        bookList.forEach(book -> bookService.updateStockCount(book.getId(),
                book.getStockCount() - bookAndOrderCountMap.get(book.getId())));

        Order order = Order.builder()
                .customer(customer)
                .orderDate(LocalDateTime.now())
                .totalPrice(totalPrice)
                .bookOrder(bookAndOrderCountMap)
                .status(OrderStatus.RECEIVED)
                .build();

        orderRepository.save(order);

        log.info("New order was created. {}", order);

        return OrderResponse.builder()
                .customerId(order.getCustomer().getId())
                .books(orderRequest.getBookInfo())
                .totalPrice(order.getTotalPrice())
                .orderDateTime(order.getOrderDate())
                .status(order.getStatus())
                .build();
    }

    @Override
    public OrderResponse getOrder(Long id) {
        Optional<Order> order = orderRepository.findById(id);

        if (!order.isPresent()) {
            StringBuilder sb = new StringBuilder();
            sb.append("There is no order with id: ").append(id);
            throw new ReadingIsGoodException(sb.toString());
        }

        List<BookOrder> bookOrders = order.get().getBookOrder().entrySet().stream()
                .map(entry -> new BookOrder(entry.getKey(), entry.getValue())).toList();

        return OrderResponse.builder()
                .customerId(order.get().getCustomer().getId())
                .books(bookOrders)
                .totalPrice(order.get().getTotalPrice())
                .orderDateTime(order.get().getOrderDate())
                .status(order.get().getStatus())
                .build();
    }

    @Override
    public List<OrderResponse> getOrdersDateInterval(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<Order> orderList = orderRepository.findByOrderDateBetween(startDateTime, endDateTime);

        if (orderList == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("There is no order between ").append(startDateTime)
                    .append(" and ").append(endDateTime);
            throw new ReadingIsGoodException(sb.toString());
        }

        return orderList.stream().map(order -> OrderResponse.builder()
                .customerId(order.getCustomer().getId())
                .books(OrderResponse.generateBookOrderListFromMap(order.getBookOrder()))
                .totalPrice(order.getTotalPrice())
                .orderDateTime(order.getOrderDate())
                .status(order.getStatus())
                .build()).toList();
    }

    @Override
    public List<OrderResponse> getAllOrdersOfTheCustomer(Long id) {
        Customer customer = customerService.getCustomerById(id);

        List<Order> allOrdersOfTheCustomer = orderRepository.findByCustomer(customer);

        return allOrdersOfTheCustomer.stream().map(order -> OrderResponse.builder()
                .customerId(order.getCustomer().getId())
                .books(OrderResponse.generateBookOrderListFromMap(order.getBookOrder()))
                .totalPrice(order.getTotalPrice())
                .orderDateTime(order.getOrderDate())
                .status(order.getStatus())
                .build()).toList();
    }

}
