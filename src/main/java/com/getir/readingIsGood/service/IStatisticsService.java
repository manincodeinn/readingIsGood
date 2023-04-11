package com.getir.readingIsGood.service;

import com.getir.readingIsGood.model.response.CustomerMonthlyStatisticsResponse;

import java.util.List;
import java.util.Optional;

public interface IStatisticsService {

    Optional<List<CustomerMonthlyStatisticsResponse>> getCustomerMonthlyStatistics(Long customerId);

}
