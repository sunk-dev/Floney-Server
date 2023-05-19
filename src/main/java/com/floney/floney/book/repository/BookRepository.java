package com.floney.floney.book.repository;

import com.floney.floney.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findBookByCodeAndStatus(String code,boolean status);

    Optional<Book> findBookByBookKeyAndStatus(String bookKey,boolean status);

}
