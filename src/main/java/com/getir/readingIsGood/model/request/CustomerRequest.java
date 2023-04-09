package com.getir.readingIsGood.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerRequest {

    @Size(min = 5, message = "Name should have least 5 characters.")
    String name;
    @Email(message = "Email is not valid.",
            regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    String email;
    @Past(message = "Date should be past.")
    LocalDate birthDate;
    @Size(min = 10, message = "Address should have least 10 characters.")
    String address;

}
