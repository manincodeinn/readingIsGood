package com.getir.readingIsGood.service.impl;

import com.getir.readingIsGood.entity.Book;
import com.getir.readingIsGood.entity.Customer;
import com.getir.readingIsGood.entity.Order;
import com.getir.readingIsGood.model.BookOrder;
import com.getir.readingIsGood.model.OrderRequestModel;
import com.getir.readingIsGood.model.ReadingIsGoodException;
import com.getir.readingIsGood.repository.IBookRepository;
import com.getir.readingIsGood.repository.ICustomerRepository;
import com.getir.readingIsGood.repository.IOrderRepository;
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
    private ICustomerRepository customerRepository;
    @Autowired
    private IBookRepository bookRepository;

    @Transactional
    @Override
    public Order createNewOrder(OrderRequestModel orderRequestModel) {
        Optional<Customer> customer = customerRepository.findById(orderRequestModel.getCustomerId());
        if (!customer.isPresent()) {
            StringBuilder sb = new StringBuilder();
            sb.append("There is no customer with id: ").append(orderRequestModel.getCustomerId());
            throw new ReadingIsGoodException(sb.toString());
        }

        List<Book> books = new ArrayList<>();
        double totalPrice = 0;
        for (BookOrder bookOrder : orderRequestModel.getBookInfo()) {
            Optional<Book> book = bookRepository.findById(bookOrder.getBookId());

            if (!book.isPresent()) {
                StringBuilder sb = new StringBuilder();
                sb.append("There is no book with id: ").append(bookOrder.getBookId());
                throw new ReadingIsGoodException(sb.toString());
            }

            int isCountEnough = book.get().getStockCount() - bookOrder.getOrderCount();
            if (isCountEnough < 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("There is no stock for book that have id: ").append(book.get().getId());
                throw new ReadingIsGoodException(sb.toString());
            }

            book.get().setStockCount(isCountEnough);
            bookRepository.save(book.get());

            totalPrice += book.get().getPrice() * bookOrder.getOrderCount();
            books.add(book.get());
        }

        Order order = Order.builder()
                .customer(customer.get())
                .books(books)
                .orderDate(LocalDateTime.now())
                .totalPrice(totalPrice)
                .build();

        return orderRepository.save(order);
    }

}
