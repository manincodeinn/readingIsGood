package com.getir.readingIsGood.model.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CustomerRequest {

    @Size(min = 5, message = "Name should have least 5 characters.")
    @NotNull
    private String name;
    @Email(message = "Email is not valid.",
            regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    @NotNull
    private String email;
    @Past(message = "Date should be past.")
    @NotNull
    private LocalDate birthDate;
    @Size(min = 10, message = "Address should have least 10 characters.")
    @NotNull
    private String address;

}
