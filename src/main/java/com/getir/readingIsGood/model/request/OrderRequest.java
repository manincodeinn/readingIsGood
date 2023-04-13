package com.getir.readingIsGood.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class OrderRequest {

    @NotNull(message = "Customer ID cannot be null.")
    private Long customerId;
    @NotNull(message = "The order should include least one book.")
    @NotEmpty(message = "The order should include least one book.")
    private List<@Valid BookOrder> bookInfo;

}
