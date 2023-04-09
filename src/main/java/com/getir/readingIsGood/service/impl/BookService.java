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
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class BookService implements IBookService {

    @Autowired
    private IBookRepository bookRepository;

    @Transactional
    @Override
    public BookResponse createNewBook(BookRequest bookRequest) {
        Optional<Book> book = Optional.ofNullable(bookRepository.findByNameAndAuthor(bookRequest.getName(),
                bookRequest.getAuthor()));

        // TODO book varsa price ve stock count guncelle
        if (book.isPresent()) {
            StringBuilder sb = new StringBuilder();
            sb.append(book.get().getName()).append(" already exists. Stock count can be updated with this id: ")
                    .append(book.get().getId());
            throw new ReadingIsGoodException(sb.toString());
        }

        Book newBook = Book.builder()
                .name(bookRequest.getName())
                .author(bookRequest.getAuthor())
                .price(bookRequest.getPrice())
                .stockCount(bookRequest.getStockCount())
                .build();

        bookRepository.save(newBook);

        log.info("New book was created. {}", newBook);

        return BookResponse.builder()
                .name(newBook.getName())
                .author(newBook.getAuthor())
                .price(newBook.getPrice())
                .stockCount(newBook.getStockCount())
                .build();
    }

    @Override
    public BookResponse getBook(Long id) {
        Optional<Book> book = bookRepository.findById(id);

        if (!book.isPresent()) {
            StringBuilder sb = new StringBuilder();
            sb.append("There is no book with id: ").append(id);
            throw new ReadingIsGoodException(sb.toString());
        }

        return BookResponse.builder()
                .name(book.get().getName())
                .author(book.get().getAuthor())
                .price(book.get().getPrice())
                .stockCount(book.get().getStockCount())
                .build();
    }

    @Transactional
    @Override
    public BookResponse updateStockCount(Long id, Integer stockCount) {
        Optional<Book> book = bookRepository.findById(id);

        if (!book.isPresent()) {
            StringBuilder sb = new StringBuilder();
            sb.append("There is no book with id: ").append(id);
            throw new ReadingIsGoodException(sb.toString());
        }

        book.get().setStockCount(stockCount);
        bookRepository.save(book.get());

        log.info("Stock count was updated. {}", book);

        return BookResponse.builder()
                .name(book.get().getName())
                .author(book.get().getAuthor())
                .price(book.get().getPrice())
                .stockCount(book.get().getStockCount())
                .build();
    }

    // TODO getAllBooks with paging

}
