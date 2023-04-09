package com.getir.readingIsGood.model.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CustomerResponse {

    private String name;
    private String email;
    private LocalDate birthDate;
    private String address;

}
