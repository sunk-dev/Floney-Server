package com.floney.floney.book.repository;

import com.floney.floney.book.domain.entity.Book;
import com.floney.floney.book.dto.process.DatesDuration;
import com.floney.floney.book.dto.response.BudgetYearResponse;

import java.util.List;
import java.util.Optional;

public interface BookCustomRepository {

    Optional<Book> findByBookUserEmailAndBookKey(String userEmail, String bookKey);

    List<BudgetYearResponse> findBudgetByYear(String bookKey, DatesDuration duration);

    List<Book> findAllByUserEmail(String userEmail);
}
