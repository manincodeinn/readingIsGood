package com.getir.readingIsGood.repository;

import com.getir.readingIsGood.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBookRepository extends JpaRepository<Book, Long> {

    boolean existsByNameAndAuthor(String name, String author);

    Book findByNameAndAuthor(String name, String author);

}
