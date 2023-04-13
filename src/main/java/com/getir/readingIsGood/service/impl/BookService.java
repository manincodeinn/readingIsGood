package com.getir.readingIsGood.service.impl;

import com.getir.readingIsGood.entity.Book;
import com.getir.readingIsGood.model.exception.ReadingIsGoodException;
import com.getir.readingIsGood.model.request.BookRequest;
import com.getir.readingIsGood.model.response.BookResponse;
import com.getir.readingIsGood.repository.IBookRepository;
import com.getir.readingIsGood.service.IBookService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class BookService implements IBookService {

    @Autowired
    private IBookRepository bookRepository;

    @Transactional
    @Override
    public Optional<BookResponse> createNewBook(BookRequest bookRequest) {
        Book newBook = null;

        try {
            boolean isBookExist = bookRepository.existsByNameAndAuthor(bookRequest.getName(), bookRequest.getAuthor());

            if (isBookExist) {
                log.warn("The book already exists. Name: {}, Author: {}", bookRequest.getName(), bookRequest.getAuthor());
                return Optional.empty();
            }

            newBook = Book.builder()
                    .name(bookRequest.getName())
                    .author(bookRequest.getAuthor())
                    .price(bookRequest.getPrice())
                    .stockCount(bookRequest.getStockCount())
                    .build();

            Book result = bookRepository.save(newBook);

            log.info("New book was created by username: {}. Book info: {}", getUsername(), result);
        } catch (Exception exception) {
            throw new ReadingIsGoodException("Error occurred while creating new book.", exception);
        }

        return Optional.ofNullable(BookResponse.builder()
                .name(newBook.getName())
                .author(newBook.getAuthor())
                .price(newBook.getPrice())
                .stockCount(newBook.getStockCount())
                .build());
    }

    @Override
    public Optional<BookResponse> getBook(Long id) {
        Optional<Book> book = null;

        try {
            book = bookRepository.findById(id);

            if (!book.isPresent()) {
                log.warn("There is no book with id: {}", id);
                return Optional.empty();
            }
        } catch (Exception exception) {
            throw new ReadingIsGoodException("Error occurred while getting book.", exception);
        }

        return Optional.ofNullable(BookResponse.builder()
                .name(book.get().getName())
                .author(book.get().getAuthor())
                .price(book.get().getPrice())
                .stockCount(book.get().getStockCount())
                .build());
    }

    @Transactional
    @Override
    public Optional<BookResponse> updateStockCount(Long id, Integer stockCount) {
        Optional<Book> book;

        try {
            book = bookRepository.findById(id);

            if (!book.isPresent()) {
                log.warn("There is no book with id: {}", id);
                return Optional.empty();
            }

            book.get().setStockCount(stockCount);
            bookRepository.save(book.get());

            log.info("Stock count of the book with id: {} was updated by username: {}", book.get().getId(), getUsername());
        } catch (Exception exception) {
            throw new ReadingIsGoodException("Error occurred while updating stock count.", exception);
        }

        return Optional.ofNullable(BookResponse.builder()
                .name(book.get().getName())
                .author(book.get().getAuthor())
                .price(book.get().getPrice())
                .stockCount(book.get().getStockCount())
                .build());
    }

    @Override
    public boolean isBookExistAndStockEnough(long id, int orderedStockCount) {
        Optional<Book> book;

        try {
            book = bookRepository.findById(id);
        } catch (Exception exception) {
            throw new ReadingIsGoodException("Error occurred while finding book by id.", exception);
        }

        return book.isPresent() && (book.get().getStockCount() >= orderedStockCount);
    }

    @Override
    public Optional<Book> getBookWithId(Long id) {
        Optional<Book> book;

        try {
            book = bookRepository.findById(id);

            if (!book.isPresent()) {
                log.warn("There is no book with id: {}", id);
                return Optional.empty();
            }
        } catch (Exception exception) {
            throw new ReadingIsGoodException("Error occurred while getting book by id.", exception);
        }

        return book;
    }

    private String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : "";
    }

}
