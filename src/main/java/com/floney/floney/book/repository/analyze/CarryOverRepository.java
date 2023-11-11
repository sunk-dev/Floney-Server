package com.floney.floney.book.repository.analyze;

import com.floney.floney.book.entity.Book;
import com.floney.floney.book.entity.CarryOver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface CarryOverRepository extends JpaRepository<CarryOver, Long>, CarryOverCustomRepository {
    Optional<CarryOver> findCarryOverByDateAndBook(LocalDate targetDate, Book book);

}