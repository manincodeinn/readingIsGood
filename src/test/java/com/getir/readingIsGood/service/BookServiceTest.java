package com.getir.readingIsGood.service;

import com.getir.readingIsGood.entity.Book;
import com.getir.readingIsGood.model.request.BookRequest;
import com.getir.readingIsGood.model.response.BookResponse;
import com.getir.readingIsGood.repository.IBookRepository;
import com.getir.readingIsGood.service.impl.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private IBookRepository bookRepository;
    @InjectMocks
    private BookService bookService;

    private static final BookRequest bookRequest = BookRequest.builder()
            .name("Getir Bi Mutluluk")
            .author("Getir Büyük")
            .price(20.15)
            .stockCount(701)
            .build();

    private static Book book = Book.builder()
            .id(12345L)
            .name(bookRequest.getName())
            .author(bookRequest.getAuthor())
            .price(bookRequest.getPrice())
            .stockCount(bookRequest.getStockCount())
            .build();

    @Test
    void createNewBookTest() {
        when(bookRepository.existsByNameAndAuthor(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(false).thenReturn(true);
        when(bookRepository.save(Mockito.any(Book.class))).thenReturn(book);

        Optional<BookResponse> newBook = bookService.createNewBook(bookRequest);

        assertNotEquals(Optional.empty(), newBook);
        assertEquals(bookRequest.getName(), newBook.get().getName());
        assertEquals(bookRequest.getAuthor(), newBook.get().getAuthor());
        assertEquals(bookRequest.getPrice(), newBook.get().getPrice());
        assertEquals(bookRequest.getStockCount(), newBook.get().getStockCount());

        assertEquals(Optional.empty(), bookService.createNewBook(bookRequest));
    }

    @Test
    void getBookTest() {
        when(bookRepository.findById(book.getId())).thenReturn(Optional.ofNullable(book));

        Optional<BookResponse> book1 = bookService.getBook(book.getId());

        assertNotEquals(Optional.empty(), book1);
        assertEquals(book.getName(), book1.get().getName());
        assertEquals(book.getAuthor(), book1.get().getAuthor());
        assertEquals(book.getPrice(), book1.get().getPrice());
        assertEquals(book.getStockCount(), book1.get().getStockCount());

        assertEquals(Optional.empty(), bookService.getBook(0L));
    }

    @Test
    void getBookWithIdTest() {
        when(bookRepository.findById(book.getId())).thenReturn(Optional.ofNullable(book));

        Optional<Book> bookWithId = bookService.getBookWithId(book.getId());

        assertNotEquals(Optional.empty(), bookWithId);
        assertEquals(book.getName(), bookWithId.get().getName());
        assertEquals(book.getAuthor(), bookWithId.get().getAuthor());
        assertEquals(book.getPrice(), bookWithId.get().getPrice());
        assertEquals(book.getStockCount(), bookWithId.get().getStockCount());

        assertEquals(Optional.empty(), bookService.getBookWithId(0L));
    }

    @Test
    void updateStockCountTest() {
        when(bookRepository.findById(book.getId())).thenReturn(Optional.ofNullable(book));
        when(bookRepository.save(Mockito.any(Book.class))).thenReturn(book);

        int updatedStockCount = 699;
        Optional<BookResponse> bookResponse = bookService.updateStockCount(book.getId(), updatedStockCount);

        assertNotEquals(Optional.empty(), bookResponse);
        assertEquals(book.getName(), bookResponse.get().getName());
        assertEquals(book.getAuthor(), bookResponse.get().getAuthor());
        assertEquals(book.getPrice(), bookResponse.get().getPrice());
        assertEquals(book.getStockCount(), bookResponse.get().getStockCount());

        assertEquals(Optional.empty(), bookService.updateStockCount(0L, 0));
    }

    @Test
    void isBookExistAndStockEnoughTest() {
        when(bookRepository.findById(book.getId())).thenReturn(Optional.ofNullable(book));

        // actual stock count: 701
        boolean bookExistAndStockEnough = bookService.isBookExistAndStockEnough(book.getId(), 670);
        boolean bookExistAndStockEnough2 = bookService.isBookExistAndStockEnough(book.getId(), 699);
        boolean bookExistAndStockEnough3 = bookService.isBookExistAndStockEnough(book.getId(), 702);

        assertEquals(bookExistAndStockEnough, true);
        assertEquals(bookExistAndStockEnough2, true);
        assertEquals(bookExistAndStockEnough3, false);
    }

}
