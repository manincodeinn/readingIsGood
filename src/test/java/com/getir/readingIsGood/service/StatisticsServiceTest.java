package com.getir.readingIsGood.service;

import com.getir.readingIsGood.entity.Customer;
import com.getir.readingIsGood.model.request.BookOrder;
import com.getir.readingIsGood.model.response.CustomerMonthlyStatisticsResponse;
import com.getir.readingIsGood.model.response.OrderResponse;
import com.getir.readingIsGood.service.impl.StatisticsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StatisticsServiceTest {

    @Mock
    private ICustomerService customerService;
    @Mock
    private IOrderService orderService;
    @InjectMocks
    private StatisticsService statisticsService;

    private static final Customer customer = Customer.builder()
            .id(12345L)
            .name("getir")
            .email("getir@getir.com")
            .birthDate(LocalDate.of(2015, 7, 1))
            .address("Getir bi mutluluk")
            .build();

    private static final OrderResponse orderResponse = OrderResponse.builder()
            .customerId(customer.getId())
            .books(Arrays.asList(new BookOrder(12345L, 3), new BookOrder(1234L, 2)))
            .orderDateTime(LocalDateTime.of(2023, 4, 8, 23, 34, 26))
            .totalPrice(20.20)
            .build();

    private static final OrderResponse orderResponse2 = OrderResponse.builder()
            .customerId(customer.getId())
            .books(Arrays.asList(new BookOrder(12345L, 2), new BookOrder(1234L, 5)))
            .orderDateTime(LocalDateTime.of(2023, 3, 8, 23, 34, 26))
            .totalPrice(60.80)
            .build();

    @Test
    void getCustomerMonthlyStatisticsTest() {
        when(customerService.getCustomerWithId(customer.getId())).thenReturn(Optional.of(customer));
        when(orderService.getAllOrdersOfTheCustomer(customer.getId(), Pageable.unpaged()))
                .thenReturn(Optional.of(Arrays.asList(orderResponse, orderResponse2)));

        Optional<List<CustomerMonthlyStatisticsResponse>> customerMonthlyStatistics =
                statisticsService.getCustomerMonthlyStatistics(customer.getId());

        assertNotEquals(Optional.empty(), customerMonthlyStatistics);
        assertEquals(2, customerMonthlyStatistics.get().size());
        assertEquals(Month.MARCH, customerMonthlyStatistics.get().get(0).getMonth());
        assertEquals(1, customerMonthlyStatistics.get().get(0).getTotalOrderCount());
        assertEquals(5, customerMonthlyStatistics.get().get(1).getTotalBookCount());
        assertEquals(20.20, customerMonthlyStatistics.get().get(1).getTotalPurchasedAmount());
    }

}
