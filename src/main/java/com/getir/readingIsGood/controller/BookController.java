package com.getir.readingIsGood.controller;

import com.getir.readingIsGood.model.request.BookRequest;
import com.getir.readingIsGood.model.response.BookResponse;
import com.getir.readingIsGood.service.IBookService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private IBookService bookService;

    @PostMapping("/create-new-book")
    public ResponseEntity<BookResponse> createNewBook(@Valid @RequestBody BookRequest bookRequest) {
        log.info("New book request was received.");
        return new ResponseEntity<>(bookService.createNewBook(bookRequest), HttpStatus.OK);
    }

    @GetMapping("/get-book")
    public ResponseEntity<BookResponse> getBook(Long id) {
        return new ResponseEntity<>(bookService.getBook(id), HttpStatus.OK);
    }

    @PutMapping("/update-stock-count")
    public ResponseEntity<BookResponse> updateStockCount(@RequestParam Long id, @RequestParam Integer stockCount) {
        log.info("Update stock count request was received.");
        return new ResponseEntity<>(bookService.updateStockCount(id, stockCount), HttpStatus.OK);
    }

}
