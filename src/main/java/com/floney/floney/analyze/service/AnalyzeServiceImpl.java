package com.floney.floney.analyze.service;

import com.floney.floney.book.dto.process.BookAnalyzer;
import com.floney.floney.book.dto.response.AnalyzeResponse;
import com.floney.floney.book.dto.request.AnalyzeByCategoryRequest;
import com.floney.floney.book.dto.request.AnalyzeRequestByAsset;
import com.floney.floney.book.dto.request.AnalyzeRequestByBudget;
import com.floney.floney.book.dto.request.DatesDuration;
import com.floney.floney.book.dto.response.AnalyzeResponseByAsset;
import com.floney.floney.book.dto.response.AnalyzeResponseByBudget;
import com.floney.floney.book.dto.response.AnalyzeResponseByCategory;
import com.floney.floney.book.entity.Book;
import com.floney.floney.book.entity.BookAnalyze;
import com.floney.floney.book.repository.BookAnalyzeRepository;
import com.floney.floney.book.repository.BookLineCustomRepository;
import com.floney.floney.book.repository.BookRepository;
import com.floney.floney.book.repository.category.CategoryRepository;
import com.floney.floney.book.util.DateFactory;
import com.floney.floney.common.constant.Status;
import com.floney.floney.common.exception.book.NotFoundBookException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyzeServiceImpl implements AnalyzeService {

    private final BookRepository bookRepository;
    private final BookLineCustomRepository bookLineRepository;
    private final BookAnalyzeRepository analyzeRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public AnalyzeResponse analyzeByCategory(AnalyzeByCategoryRequest request) {
        List<AnalyzeResponseByCategory> analyzeResultByCategory = bookLineRepository.analyzeByCategory(request);
        Book savedBook = findBook(request.getBookKey());
        BookAnalyze savedAnalyze = saveAnalyze(request, analyzeResultByCategory, savedBook);
        long difference = calculateDifference(request, savedAnalyze);

        return AnalyzeResponse.of(analyzeResultByCategory, savedAnalyze, difference);
    }

    @Override
    @Transactional(readOnly = true)
    public AnalyzeResponseByBudget analyzeByBudget(AnalyzeRequestByBudget request) {
        DatesDuration duration = DateFactory.getDateDuration(request.getDate());
        return bookLineRepository.totalIncomeForBudget(request, duration);
    }

    @Override
    @Transactional(readOnly = true)
    public AnalyzeResponseByAsset analyzeByAsset(AnalyzeRequestByAsset request) {
        Book savedBook = findBook(request.getBookKey());
        long initAsset = savedBook.getInitAsset();

        Map<String, Long> totalExpense = bookLineRepository.totalExpensesForAsset(request);
        BookAnalyzer bookAnalyzer = new BookAnalyzer(totalExpense);
        return bookAnalyzer.analyzeAsset(initAsset);
    }

    @Override
    @Transactional(readOnly = true)
    public Book findBook(String bookKey) {
        return bookRepository.findBookByBookKeyAndStatus(bookKey, Status.ACTIVE)
            .orElseThrow(NotFoundBookException::new);
    }

    private long calculateDifference(AnalyzeByCategoryRequest request, BookAnalyze currentMonthAnalyze) {
        long beforeMonthTotal = bookLineRepository.totalExpenseForBeforeMonth(request);
        return currentMonthAnalyze.calculateDifferenceWith(beforeMonthTotal);
    }

    private BookAnalyze saveAnalyze(AnalyzeByCategoryRequest request, List<AnalyzeResponseByCategory> analyzeResult, Book book) {
        BookAnalyze analyze = BookAnalyze.builder()
            .analyzeDate(request.getLocalDate())
            .book(book)
            .category(categoryRepository.findFlowCategory(request.getRoot()))
            .analyzeResult(analyzeResult)
            .build();

        return analyzeRepository.save(analyze);
    }
}
