package com.getir.readingIsGood.service.impl;

import com.getir.readingIsGood.entity.Customer;
import com.getir.readingIsGood.model.exception.ReadingIsGoodException;
import com.getir.readingIsGood.model.response.CustomerMonthlyStatisticsResponse;
import com.getir.readingIsGood.model.response.OrderResponse;
import com.getir.readingIsGood.service.ICustomerService;
import com.getir.readingIsGood.service.IOrderService;
import com.getir.readingIsGood.service.IStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StatisticsService implements IStatisticsService {

    @Autowired
    private ICustomerService customerService;
    @Autowired
    private IOrderService orderService;

    @Override
    public Optional<List<CustomerMonthlyStatisticsResponse>> getCustomerMonthlyStatistics(Long customerId) {
        List<CustomerMonthlyStatisticsResponse> customerMonthlyStatisticsResponseList = null;

        try {
            Optional<Customer> customer = customerService.getCustomerWithId(customerId);

            if (customer.isEmpty()) {
                log.warn("There is no customer with id: {}", customerId);
                return Optional.empty();
            }

            Optional<List<OrderResponse>> allOrdersOfTheCustomer =
                    orderService.getAllOrdersOfTheCustomer(customerId, Pageable.unpaged());

            if (allOrdersOfTheCustomer.isEmpty() || allOrdersOfTheCustomer.get().size() == 0) {
                log.info("There is no order of the customer with id: {}", customerId);
                return Optional.empty();
            }

            Map<Month, List<OrderResponse>> groupOrdersByMonth = allOrdersOfTheCustomer.get().stream()
                    .collect(Collectors.groupingBy(orderResponse -> orderResponse.getOrderDateTime().getMonth()));

            customerMonthlyStatisticsResponseList = groupOrdersByMonth.entrySet().stream()
                    .map(monthOrderListEntry -> CustomerMonthlyStatisticsResponse.builder()
                            .month(monthOrderListEntry.getKey())
                            .totalOrderCount(monthOrderListEntry.getValue().size())
                            .totalBookCount(
                                    monthOrderListEntry.getValue().stream()
                                            .map(orderResponse -> orderResponse.getBooks().stream()
                                                    .map(bookOrder -> bookOrder.getOrderCount())
                                                    .collect(Collectors.summingInt(Integer::intValue)))
                                            .collect(Collectors.summingInt(Integer::intValue))
                            )
                            .totalPurchasedAmount(
                                    monthOrderListEntry.getValue().stream()
                                            .map(orderResponse -> orderResponse.getTotalPrice())
                                            .collect(Collectors.summingDouble(Double::doubleValue))
                            )
                            .build()
                    ).collect(Collectors.toList());

            customerMonthlyStatisticsResponseList.
                    sort(Comparator.comparing(CustomerMonthlyStatisticsResponse::getMonth));
        } catch (Exception exception) {
            throw new ReadingIsGoodException("Error occurred while getting customer monthly statistics.", exception);
        }

        return Optional.of(customerMonthlyStatisticsResponseList);
    }

}
