package com.floney.floney.book;

import com.floney.floney.book.dto.constant.CategoryEnum;
import com.floney.floney.book.dto.process.BookLineExpense;
import com.floney.floney.book.dto.process.TotalExpense;
import com.floney.floney.book.dto.request.AllOutcomesRequest;
import com.floney.floney.book.dto.request.DatesDuration;
import com.floney.floney.book.entity.*;
import com.floney.floney.book.entity.category.BookCategory;
import com.floney.floney.book.repository.BookLineCategoryRepository;
import com.floney.floney.book.repository.BookLineRepository;
import com.floney.floney.book.repository.BookRepository;
import com.floney.floney.book.repository.BookUserRepository;
import com.floney.floney.book.repository.category.CategoryRepository;
import com.floney.floney.config.TestConfig;
import com.floney.floney.config.UserFixture;
import com.floney.floney.user.entity.User;
import com.floney.floney.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.floney.floney.book.BookFixture.*;
import static com.floney.floney.book.BookLineFixture.*;
import static com.floney.floney.book.CategoryFixture.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfig.class)
public class BookLineRepositoryTest {

    @Autowired
    private BookLineRepository bookLineRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookUserRepository bookUserRepository;
    @Autowired
    private BookLineCategoryRepository bookLineCategoryRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    private Book book;
    private User user;

    private Category incomeCategory;
    private Category outcomeCategory;

    private BookCategory childIncomeCategory;

    @BeforeEach
    void init() {
        user = userRepository.save(UserFixture.createUser());
        book = bookRepository.save(BookFixture.createBook());
        incomeCategory = categoryRepository.save(incomeBookCategory());
        outcomeCategory = categoryRepository.save(outComeBookCategory());
        childIncomeCategory = categoryRepository.save(createChildCategory(incomeCategory, book));
    }

    @Test
    @DisplayName("각 가계부 내역 지정된 날짜기간의 수입/지출의 총합을 조회한다")
    void income() {
        BookLine bookLine = bookLineRepository.save(createBookLine(book, 1000L));
        BookLine bookLine2 = bookLineRepository.save(createBookLine(book, 1000L));

        BookLineCategory category = bookLineCategoryRepository.save(createLineCategory((DefaultCategory) incomeCategory, bookLine));
        bookLine.add(CategoryEnum.FLOW, category);

        BookLineCategory category2 = bookLineCategoryRepository.save(createLineCategory((DefaultCategory) outcomeCategory, bookLine2));
        bookLine2.add(CategoryEnum.FLOW, category2);

        bookLineRepository.save(bookLine);
        bookLineRepository.save(bookLine2);

        LocalDate start = LocalDate.of(2023, 10, 21);
        LocalDate end = LOCAL_DATE;

        DatesDuration dates = DatesDuration.builder()
            .startDate(start)
            .endDate(end)
            .build();

        BookLineExpense income = BookLineExpense.builder()
            .money(1000L)
            .assetType("수입")
            .date(LOCAL_DATE)
            .build();

        BookLineExpense outcome = BookLineExpense.builder()
            .money(1000L)
            .assetType("지출")
            .date(LOCAL_DATE)
            .build();

        Assertions.assertThat(bookLineRepository.dayIncomeAndOutcome(BOOK_KEY, dates))
            .isEqualTo(Arrays.asList(income, outcome));

    }

    @Test
    @DisplayName("각 가계부 내역 지정된 달의 총수입/총지출을 조회한다")
    void all_expenses() {
        BookLine bookLine = bookLineRepository.save(createBookLine(book, 1000L));
        BookLine bookLine2 = bookLineRepository.save(createBookLine(book, 1000L));

        BookLineCategory category = bookLineCategoryRepository.save(createLineCategory((DefaultCategory) incomeCategory, bookLine));
        bookLine.add(CategoryEnum.FLOW, category);

        BookLineCategory category2 = bookLineCategoryRepository.save(createLineCategory((DefaultCategory) outcomeCategory, bookLine2));
        bookLine2.add(CategoryEnum.FLOW, category2);

        bookLineRepository.save(bookLine);
        bookLineRepository.save(bookLine2);

        LocalDate start = LocalDate.of(2023, 10, 1);
        LocalDate end = LOCAL_DATE;

        DatesDuration dates = DatesDuration.builder()
            .startDate(start)
            .endDate(end)
            .build();

        Map<String, Long> totals = new HashMap<>();
        totals.put("수입", 1000L);
        totals.put("지출", 1000L);

        Assertions.assertThat(bookLineRepository.totalExpenseByMonth(BOOK_KEY, dates))
            .isEqualTo(totals);

    }

    @Test
    @DisplayName("날짜별로 총수입/총지출을 조회한다")
    void day_expenses() {
        BookLine bookLine = bookLineRepository.save(createBookLine(book, 1000L));
        BookLine bookLine2 = bookLineRepository.save(createBookLine(book, 1000L));

        BookLineCategory category = bookLineCategoryRepository.save(createLineCategory((DefaultCategory) incomeCategory, bookLine));
        bookLine.add(CategoryEnum.FLOW, category);

        BookLineCategory category2 = bookLineCategoryRepository.save(createLineCategory((DefaultCategory) outcomeCategory, bookLine2));
        bookLine2.add(CategoryEnum.FLOW, category2);

        bookLineRepository.save(bookLine);
        bookLineRepository.save(bookLine2);

        LocalDate target = LOCAL_DATE;

        TotalExpense income = TotalExpense.builder()
            .money(1000L)
            .assetType("수입")
            .build();

        TotalExpense outcome = TotalExpense.builder()
            .money(1000L)
            .assetType("지출")
            .build();

        Assertions.assertThat(bookLineRepository.totalExpenseByDay(target, BOOK_KEY))
            .isEqualTo(Arrays.asList(income, outcome));
    }

    @Test
    @DisplayName("날짜 별로 가계부 내역과 연관된 모든 카테고리와 금액을 조회한다")
    void days_line() {
        BookUser bookUser = bookUserRepository.save(createBookUser(user, book));
        BookLine bookLine = bookLineRepository.save(createBookLineWith(bookUser, book, 1000L));
        BookLine bookLine2 = bookLineRepository.save(createBookLineWith(bookUser, book, 1000L));

        BookLineCategory incomeBookLineCategory = bookLineCategoryRepository.save(createLineCategory((DefaultCategory) incomeCategory, bookLine));
        BookLineCategory childLineCategory = bookLineCategoryRepository.save(createChildLineCategory(childIncomeCategory, bookLine));
        bookLine.add(CategoryEnum.FLOW, incomeBookLineCategory);
        bookLine.add(CategoryEnum.FLOW_LINE, childLineCategory);

        BookLineCategory bookLineCategory2 = bookLineCategoryRepository.save(createLineCategory((DefaultCategory) outcomeCategory, bookLine2));
        bookLine2.add(CategoryEnum.FLOW, bookLineCategory2);

        bookLineRepository.save(bookLine);
        bookLineRepository.save(bookLine2);

        LocalDate targetDate = LOCAL_DATE;
        Assertions.assertThat(bookLineRepository.allLinesByDay(targetDate, BOOK_KEY).size())
            .isEqualTo(3);

    }

    @Test
    @DisplayName("정산에 참여하는 유저가 사용한 내역만 기간 내에 조회")
    void all_dates_with_bookUsers() {
        BookUser bookUser = bookUserRepository.save(BookFixture.createBookUser(user, book));

        BookLine bookLine = bookLineRepository.save(createBookLineWithWriter(book, 1000L, bookUser));
        BookLine bookLine2 = bookLineRepository.save(createBookLineWithWriter(book, 1000L, bookUser));

        bookLineRepository.save(bookLine);
        bookLineRepository.save(bookLine2);

        LocalDate start = LocalDate.of(2023, 10, 21);
        LocalDate end = LOCAL_DATE;

        DatesDuration datesRequest = DatesDuration.builder()
            .startDate(start)
            .endDate(end)
            .build();
        AllOutcomesRequest request = new AllOutcomesRequest(BOOK_KEY, Arrays.asList(EMAIL), datesRequest);

        Assertions.assertThat(bookLineRepository.allOutcomes(request).size())
            .isEqualTo(2);
    }

//    @Test
//    @DisplayName("카테고리 별 해당 기간의 합계를 구한다")
//    void all_category() {
//
//        BookUser writer = bookUserRepository.save(BookFixture.createBookUser(user, book));
//
//        BookLine bookLine = bookLineRepository.save(createBookLineWithWriter(book, 1000L, writer));
//        BookLine bookLine2 = bookLineRepository.save(createBookLineWithWriter(book, 3000L, writer));
//
//        //bookLine과 카테고리 매핑한 BookLineCategory 객체
//        BookLineCategory incomeBookLineCategory = bookLineCategoryRepository.save(createLineCategory((DefaultCategory) incomeCategory, bookLine));
//        BookLineCategory childLineCategory = bookLineCategoryRepository.save(createChildLineCategory(childIncomeCategory, bookLine));
//
//        BookLineCategory incomeBookLineCategory2 = bookLineCategoryRepository.save(createLineCategory((DefaultCategory) incomeCategory, bookLine2));
//        BookLineCategory childLineCategory2 = bookLineCategoryRepository.save(createChildLineCategory(childIncomeCategory, bookLine2));
//
//        //bookLine과 연관
//        bookLine.add(CategoryEnum.FLOW, incomeBookLineCategory);
//        bookLine.add(CategoryEnum.FLOW_LINE, childLineCategory);
//
//        bookLine2.add(CategoryEnum.FLOW, incomeBookLineCategory2);
//        bookLine2.add(CategoryEnum.FLOW_LINE, childLineCategory2);
//
//        bookLineRepository.save(bookLine);
//        bookLineRepository.save(bookLine2);
//
//        LocalDate start = LocalDate.of(2023, 10, 21);
//        LocalDate end = LOCAL_DATE;
//
//        DatesRequest datesRequest = DatesRequest.builder()
//            .startDate(start)
//            .endDate(end)
//            .build();
//
//        List<AnalyzeByCategoryResponse> result = bookLineRepository.analyzeByCategory(incomeCategory.getName(), book.getBookKey(), datesRequest);
//        System.out.println(result);
//    }

}


