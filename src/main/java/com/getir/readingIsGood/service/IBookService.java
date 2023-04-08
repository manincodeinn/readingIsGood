package com.getir.readingIsGood.service;

import com.getir.readingIsGood.entity.Book;

public interface IBookService {

    Book createNewBook(Book book);

    Book getBook(Long id);

    Book updateStockCount(Book book);

}
