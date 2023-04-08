package com.getir.readingIsGood.service.impl;

import com.getir.readingIsGood.entity.Book;
import com.getir.readingIsGood.model.ReadingIsGoodException;
import com.getir.readingIsGood.repository.IBookRepository;
import com.getir.readingIsGood.service.IBookService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class BookService implements IBookService {

    @Autowired
    private IBookRepository bookRepository;

    @Transactional
    @Override
    public Book createNewBook(Book book) {
        Optional<Book> existBook = Optional.ofNullable(bookRepository.findByNameAndAuthor(book.getName(),
                book.getAuthor()));

        if (existBook.isPresent()) {
            StringBuilder sb = new StringBuilder();
            sb.append(book.getName()).append(" already exists. Stock count can be updated with this id: ")
                    .append(existBook.get().getId());
            throw new ReadingIsGoodException(sb.toString());
        }

        return bookRepository.save(book);
    }

    @Override
    public Book getBook(Long id) {
        Optional<Book> book = bookRepository.findById(id);

        if (!book.isPresent()) {
            StringBuilder sb = new StringBuilder();
            sb.append("There is no book with id: ").append(id);
            throw new ReadingIsGoodException(sb.toString());
        }

        return book.get();
    }

    @Transactional
    @Override
    public Book updateStockCount(Book book) {
        Optional<Book> existBook = bookRepository.findById(book.getId());

        if (!existBook.isPresent()) {
            StringBuilder sb = new StringBuilder();
            sb.append("There is no book with id: ").append(book.getId());
            throw new ReadingIsGoodException(sb.toString());
        }

        existBook.get().setStockCount(book.getStockCount());

        return existBook.get();
    }

}
