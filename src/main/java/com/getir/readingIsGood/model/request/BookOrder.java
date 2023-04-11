package com.getir.readingIsGood.model.request;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookOrder {

    @NotNull(message = "Book ID cannot be null.")
    private Long bookId;
    @Positive(message = "The book count in the order have to be greater than 0.")
    @NotNull(message = "The book count in the order have to be greater than 0.")
    private Integer orderCount;

}
