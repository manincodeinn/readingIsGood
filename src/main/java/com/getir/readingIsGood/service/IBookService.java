package com.getir.readingIsGood.service;

import com.getir.readingIsGood.entity.Book;
import com.getir.readingIsGood.model.request.BookRequest;
import com.getir.readingIsGood.model.response.BookResponse;

public interface IBookService {

    BookResponse createNewBook(BookRequest bookRequest);

    BookResponse getBook(Long id);

    BookResponse updateStockCount(Long id, Integer stockCount);

}
