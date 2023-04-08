package com.getir.readingIsGood.controller;

import com.getir.readingIsGood.entity.Book;
import com.getir.readingIsGood.service.IBookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private IBookService bookService;

    @PostMapping("/create-new-book")
    public ResponseEntity<Book> createNewBook(@Valid @RequestBody Book book) {
        return new ResponseEntity<>(bookService.createNewBook(book), HttpStatus.OK);
    }

    @GetMapping("/get-book")
    public ResponseEntity<Book> getBook(Long id) {
        return new ResponseEntity<>(bookService.getBook(id), HttpStatus.OK);
    }

    @PostMapping("/update-stock-count")
    public ResponseEntity<Book> updateStockCount(@RequestBody Book book) {
        return new ResponseEntity<>(bookService.updateStockCount(book), HttpStatus.OK);
    }

}
