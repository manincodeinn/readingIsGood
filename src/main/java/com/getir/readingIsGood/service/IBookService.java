package com.getir.readingIsGood.service;

import com.getir.readingIsGood.entity.Book;
import com.getir.readingIsGood.model.request.BookRequest;
import com.getir.readingIsGood.model.response.BookResponse;

import java.util.Optional;

public interface IBookService {

    Optional<BookResponse> createNewBook(BookRequest bookRequest);

    Optional<BookResponse> getBook(Long id);

    Optional<BookResponse> updateStockCount(Long id, Integer stockCount);

    boolean isBookExistAndStockEnough(long id, int orderedStockCount);

    Optional<Book> getBookWithId(Long id);

}
