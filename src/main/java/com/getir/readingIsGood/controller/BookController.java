package com.getir.readingIsGood.controller;

import com.getir.readingIsGood.model.request.BookRequest;
import com.getir.readingIsGood.model.response.BookResponse;
import com.getir.readingIsGood.service.IBookService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private IBookService bookService;

    @PostMapping("/create-new-book")
    public ResponseEntity<BookResponse> createNewBook(@Valid @RequestBody BookRequest bookRequest) {
        log.info("Create new book request was received.");

        Optional<BookResponse> newBook = bookService.createNewBook(bookRequest);

        return newBook.map(bookResponse -> new ResponseEntity<>(bookResponse, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/get-book")
    public ResponseEntity<BookResponse> getBook(@RequestParam @NotNull Long id) {
        Optional<BookResponse> book = bookService.getBook(id);

        return book.map(bookResponse -> new ResponseEntity<>(bookResponse, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PutMapping("/update-stock-count")
    public ResponseEntity<BookResponse> updateStockCount(@RequestParam @NotNull Long id,
                                                         @RequestParam @NotNull Integer stockCount) {
        log.info("Update stock count request was received.");

        Optional<BookResponse> bookResponse = bookService.updateStockCount(id, stockCount);

        return bookResponse.map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

}
