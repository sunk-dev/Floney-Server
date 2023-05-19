package com.floney.floney.book.repository;

import com.floney.floney.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findBookByCodeAndStatus(String code,boolean status);

    Book findBookByProviderEmail(String email);

    Optional<Book> findBookByBookKey(String bookKey);

}
