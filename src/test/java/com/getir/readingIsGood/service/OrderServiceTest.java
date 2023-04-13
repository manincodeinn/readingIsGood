package com.getir.readingIsGood.service;

import com.getir.readingIsGood.entity.Book;
import com.getir.readingIsGood.entity.Customer;
import com.getir.readingIsGood.entity.Order;
import com.getir.readingIsGood.model.enums.OrderStatus;
import com.getir.readingIsGood.model.request.BookOrder;
import com.getir.readingIsGood.model.request.OrderRequest;
import com.getir.readingIsGood.model.response.OrderResponse;
import com.getir.readingIsGood.repository.IOrderRepository;
import com.getir.readingIsGood.service.impl.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.AdditionalMatchers.leq;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private IOrderRepository orderRepository;
    @Mock
    private ICustomerService customerService;
    @Mock
    private IBookService bookService;
    @InjectMocks
    private OrderService orderService;

    private static final Customer customer = Customer.builder()
            .id(12345L)
            .name("getir")
            .email("getir@getir.com")
            .birthDate(LocalDate.of(2015, 7, 1))
            .address("Getir bi mutluluk")
            .build();

    private static Book book = Book.builder()
            .id(12345L)
            .name("Getir Bi Mutluluk")
            .author("Getir Büyük")
            .price(20.15)
            .stockCount(701)
            .build();

    private static Book book2 = Book.builder()
            .id(1234L)
            .name("Getir Bi Mutluluk2")
            .author("Getir Büyük")
            .price(20.16)
            .stockCount(601)
            .build();

    private static Book book3 = Book.builder()
            .id(123L)
            .name("Getir Bi Mutluluk3")
            .author("Getir Büyük")
            .price(20.17)
            .stockCount(501)
            .build();

    private static List<BookOrder> bookOrders = Arrays.asList(
            new BookOrder(book.getId(), 3), // totalPrice -> 3*20.15=60.45
            new BookOrder(book2.getId(), 2), // totalPrice -> 2*20.16=40.32
            new BookOrder(book3.getId(), 1)); // totalPrice -> 20.17
    // =====> total -> 120,94

    private static Order order = Order.builder()
            .id(12345L)
            .customer(customer)
            .bookOrder(Map.of(book.getId(), 3, book2.getId(), 2, book3.getId(), 1))
            .orderDate(LocalDateTime.now())
            .totalPrice(120.94)
            .status(OrderStatus.RECEIVED)
            .build();

    private static Order order2 = Order.builder()
            .id(1234L)
            .customer(customer)
            .bookOrder(Map.of(book.getId(), 3, book2.getId(), 2, book3.getId(), 1))
            .orderDate(LocalDateTime.now())
            .totalPrice(120.94)
            .status(OrderStatus.RECEIVED)
            .build();

    private static OrderRequest orderRequest = OrderRequest.builder()
            .customerId(customer.getId())
            .bookInfo(bookOrders)
            .build();

    @Test
    void createNewOrderTest() {
        when(customerService.getCustomerWithId(customer.getId())).thenReturn(Optional.of(customer));

        int stockCount = book.getStockCount();
        when(bookService.isBookExistAndStockEnough(eq(book.getId()), leq(stockCount))).thenReturn(true);
        when(bookService.getBookWithId(book.getId())).thenReturn(Optional.ofNullable(book));

        int stockCount2 = book2.getStockCount();
        when(bookService.isBookExistAndStockEnough(eq(book2.getId()), leq(stockCount2))).thenReturn(true);
        when(bookService.getBookWithId(book2.getId())).thenReturn(Optional.ofNullable(book2));

        int stockCount3 = book3.getStockCount();
        when(bookService.isBookExistAndStockEnough(eq(book3.getId()), leq(stockCount3))).thenReturn(true);
        when(bookService.getBookWithId(book3.getId())).thenReturn(Optional.ofNullable(book3));

        when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);

        Optional<OrderResponse> newOrder = orderService.createNewOrder(orderRequest);

        assertNotEquals(Optional.empty(), newOrder);
        assertEquals(customer.getId(), newOrder.get().getCustomerId());
        assertEquals(book.getId(), newOrder.get().getBooks().get(0).getBookId());
        assertEquals(3, newOrder.get().getBooks().get(0).getOrderCount());
        assertEquals(book2.getId(), newOrder.get().getBooks().get(1).getBookId());
        assertEquals(2, newOrder.get().getBooks().get(1).getOrderCount());
        assertEquals(book3.getId(), newOrder.get().getBooks().get(2).getBookId());
        assertEquals(1, newOrder.get().getBooks().get(2).getOrderCount());
        assertEquals(bookOrders.size(), newOrder.get().getBooks().size());
        assertEquals(120.94, newOrder.get().getTotalPrice());
    }

    @Test
    void getOrderTest() {
        when(orderRepository.findById(order.getId())).thenReturn(Optional.ofNullable(order));

        Optional<OrderResponse> order1 = orderService.getOrder(order.getId());

        assertNotEquals(Optional.empty(), order1);
        assertEquals(customer.getId(), order1.get().getCustomerId());
        assertEquals(bookOrders.size(), order1.get().getBooks().size());
        assertEquals(120.94, order1.get().getTotalPrice());
    }

    @Test
    void getOrdersDateIntervalTest() {
        when(orderRepository.findByOrderDateBetween(Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class),
                Mockito.any(Pageable.class))).thenReturn(Arrays.asList(order, order2));

        Optional<List<OrderResponse>> ordersDateInterval = orderService.getOrdersDateInterval(
                LocalDateTime.of(2022, 4, 8, 23, 34, 26),
                LocalDateTime.of(2024, 4, 8, 23, 34, 26),
                Pageable.unpaged());

        // end date is before start date
        Optional<List<OrderResponse>> ordersDateInterval2 = orderService.getOrdersDateInterval(
                LocalDateTime.of(2022, 4, 8, 23, 34, 26),
                LocalDateTime.of(2017, 4, 8, 23, 34, 26),
                Pageable.unpaged());

        assertNotEquals(Optional.empty(), ordersDateInterval);
        assertEquals(2, ordersDateInterval.get().size());
        assertEquals(order.getCustomer().getId(), ordersDateInterval.get().get(0).getCustomerId());
        assertEquals(order2.getCustomer().getId(), ordersDateInterval.get().get(1).getCustomerId());
        assertEquals(order.getTotalPrice(), ordersDateInterval.get().get(0).getTotalPrice());
        assertEquals(order2.getTotalPrice(), ordersDateInterval.get().get(1).getTotalPrice());

        assertEquals(Optional.empty(), ordersDateInterval2);
    }

    @Test
    void getAllOrdersOfTheCustomerTest() {
        when(customerService.getCustomerWithId(customer.getId())).thenReturn(Optional.of(customer));
        when(orderRepository.findByCustomer(customer, Pageable.unpaged()))
                .thenReturn(new PageImpl<>(Arrays.asList(order, order2)));

        Optional<List<OrderResponse>> allOrdersOfTheCustomer =
                orderService.getAllOrdersOfTheCustomer(customer.getId(), Pageable.unpaged());

        assertNotEquals(Optional.empty(), allOrdersOfTheCustomer);
        assertEquals(customer.getId(), allOrdersOfTheCustomer.get().get(0).getCustomerId());
        assertEquals(customer.getId(), allOrdersOfTheCustomer.get().get(1).getCustomerId());
        assertEquals(2, allOrdersOfTheCustomer.get().size());
        assertEquals(order.getTotalPrice(), allOrdersOfTheCustomer.get().get(0).getTotalPrice());
        assertEquals(order2.getTotalPrice(), allOrdersOfTheCustomer.get().get(1).getTotalPrice());
    }

}
