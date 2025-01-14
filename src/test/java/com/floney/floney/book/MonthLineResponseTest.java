package com.floney.floney.book;

import com.floney.floney.book.dto.process.BookLineExpense;
import com.floney.floney.book.dto.response.MonthLinesResponse;
import com.floney.floney.fixture.BookLineFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class MonthLineResponseTest {

    @Test
    @DisplayName("가계부 총내역이 DB에 존재하는 경우 갱신한다")
    void response() {
        List<BookLineExpense> savedExpense = Arrays.asList(BookLineFixture.createBookLineExpense());
        List<BookLineExpense> saved = MonthLinesResponse.reflectDB("2023-10-01", savedExpense);
        Assertions.assertThat(saved.get(42)).isEqualTo(savedExpense.get(0));
    }
}
