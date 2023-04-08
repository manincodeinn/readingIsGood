package com.getir.readingIsGood.service.impl;

import com.getir.readingIsGood.entity.Book;
import com.getir.readingIsGood.entity.Customer;
import com.getir.readingIsGood.entity.Order;
import com.getir.readingIsGood.model.BookOrder;
import com.getir.readingIsGood.model.OrderRequestModel;
import com.getir.readingIsGood.model.ReadingIsGoodException;
import com.getir.readingIsGood.repository.IOrderRepository;
import com.getir.readingIsGood.service.IBookService;
import com.getir.readingIsGood.service.ICustomerService;
import com.getir.readingIsGood.service.IOrderService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public Order createNewOrder(OrderRequestModel orderRequestModel) {
        Customer customer = customerService.getCustomer(orderRequestModel.getCustomerId());
        List<Book> bookList = new ArrayList<>();
        double totalPrice = 0;

        for (BookOrder bookOrder : orderRequestModel.getBookInfo()) {
            Book book = bookService.getBook(bookOrder.getBookId());

            book.setStockCount(isStockEnough(book.getId(), book.getStockCount(), bookOrder.getOrderCount()));
            bookService.updateStockCount(book);

            totalPrice += book.getPrice() * bookOrder.getOrderCount();
            bookList.add(book);
        }

        Order order = Order.builder()
                .customer(customer)
                .books(bookList)
                .orderDate(LocalDateTime.now())
                .totalPrice(totalPrice)
                .build();

        return orderRepository.save(order);
    }

    @Override
    public Order getOrder(Long id) {
        Optional<Order> order = orderRepository.findById(id);

        if(!order.isPresent()) {
            StringBuilder sb = new StringBuilder();
            sb.append("There is no order with id: ").append(id);
            throw new ReadingIsGoodException(sb.toString());
        }

        return order.get();
    }

    @Override
    public List<Order> getOrdersDateInterval(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<Order> orderList = orderRepository.findByOrderDateBetween(startDateTime, endDateTime);

        if(orderList == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("There is no order between ").append(startDateTime)
                    .append(" and ").append(endDateTime);
            throw new ReadingIsGoodException(sb.toString());
        }

        return orderList;
    }

    private int isStockEnough(Long id, int bookStockCount, int requestedStockCount) {
        int isCountEnough = bookStockCount - requestedStockCount;

        if (isCountEnough < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("There is no stock for book that have id: ").append(id);
            throw new ReadingIsGoodException(sb.toString());
        }

        return isCountEnough;
    }

}
