package com.getir.readingIsGood.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Month;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerMonthlyStatisticsResponse {

    private Month month;
    private Integer totalOrderCount;
    private Integer totalBookCount;
    private Double totalPurchasedAmount;

}
