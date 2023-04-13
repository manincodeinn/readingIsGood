package com.getir.readingIsGood.controller;

import com.getir.readingIsGood.model.response.CustomerMonthlyStatisticsResponse;
import com.getir.readingIsGood.service.IStatisticsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/statistics")
@SecurityRequirement(name = "basicAuth")
public class StatisticsController {

    @Autowired
    private IStatisticsService statisticsService;

    @GetMapping("get-customer-monthly-statistics")
    public ResponseEntity<List<CustomerMonthlyStatisticsResponse>> getCustomerMonthlyStatistics(@RequestParam @NotNull Long id) {
        Optional<List<CustomerMonthlyStatisticsResponse>> customerMonthlyStatistics =
                statisticsService.getCustomerMonthlyStatistics(id);

        return customerMonthlyStatistics.map(customerMonthlyStatisticsResponses ->
                        new ResponseEntity<>(customerMonthlyStatisticsResponses, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

}
