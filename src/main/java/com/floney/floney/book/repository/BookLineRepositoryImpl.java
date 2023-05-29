package com.floney.floney.book.repository;

import com.floney.floney.book.dto.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.floney.floney.book.dto.constant.AssetType.INCOME;
import static com.floney.floney.book.dto.constant.AssetType.OUTCOME;
import static com.floney.floney.book.entity.QBook.book;
import static com.floney.floney.book.entity.QBookLine.bookLine;
import static com.floney.floney.book.entity.QBookLineCategory.bookLineCategory;
import static com.floney.floney.book.entity.QBookUser.bookUser;
import static com.floney.floney.book.util.DateFactory.END;
import static com.floney.floney.book.util.DateFactory.START;

@Repository
@RequiredArgsConstructor
public class BookLineRepositoryImpl implements BookLineCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private static final boolean ACTIVE = true;

    @Override
    public List<TotalExpense> totalExpense(String bookKey, Map<String, LocalDate> dates) {
        return jpaQueryFactory.select(
                new QTotalExpense(
                    bookLine.money.sum(),
                    bookLineCategory.name
                )
            )
            .from(bookLine)
            .innerJoin(bookLine.book, book)
            .innerJoin(bookLine.bookLineCategories, bookLineCategory)
            .where(
                bookLine.lineDate.between(dates.get(START),dates.get(END)),
                bookLineCategory.name.in(INCOME.getKind(), OUTCOME.getKind()),
                book.bookKey.eq(bookKey),
                book.status.eq(ACTIVE),
                bookLine.status.eq(ACTIVE)
            )
            .groupBy(bookLineCategory.name)
            .orderBy(bookLineCategory.name.asc())
            .fetch();
    }

    @Override
    public List<DayLine> allLinesByDay(LocalDate date, String bookKey) {
        return jpaQueryFactory.select(
                new QDayLine(
                    bookLine.id,
                    bookLine.money,
                    bookLine.description,
                    bookLineCategory.name,
                    bookUser.profileImg
                ))
            .from(bookLine)
            .innerJoin(bookLine.bookLineCategories, bookLineCategory)
            .innerJoin(bookLine.book, book)
            .innerJoin(bookLine.writer, bookUser)
            .where(
                book.status.eq(ACTIVE),
                bookLine.status.eq(ACTIVE),
                bookLine.lineDate.eq(date),
                book.bookKey.eq(bookKey)
            )
            .groupBy(bookLine.id, bookLineCategory.name)
            .fetch();
    }

    @Override
    public List<TotalExpense> totalExpenseByDay(LocalDate date, String bookKey) {
        return jpaQueryFactory.select(
                new QTotalExpense(
                    bookLine.money.sum(),
                    bookLineCategory.name
                )
            )
            .from(bookLine)
            .innerJoin(bookLine.book, book)
            .innerJoin(bookLine.bookLineCategories,
                bookLineCategory)
            .where(
                bookLine.status.eq(ACTIVE),
                book.status.eq(ACTIVE),
                bookLine.lineDate.eq(date),
                bookLineCategory.name.in(INCOME.getKind(), OUTCOME.getKind()),
                book.bookKey.eq(bookKey)
            )
            .groupBy(bookLineCategory.name)
            .orderBy(bookLineCategory.name.asc())
            .fetch();
    }

    @Override
    public List<BookLineExpense> dayIncomeAndOutcome(String bookKey, Map<String, LocalDate> dates) {
        return jpaQueryFactory.select(
                new QBookLineExpense(
                    bookLine.lineDate,
                    bookLine.money.sum(),
                    bookLineCategory.name
                )
            )
            .from(bookLine)
            .innerJoin(bookLine.book, book)
            .innerJoin(bookLine.bookLineCategories, bookLineCategory)
            .where(
                bookLine.status.eq(ACTIVE),
                book.status.eq(ACTIVE),
                bookLine.lineDate.between(dates.get(START), dates.get(END)),
                bookLineCategory.name.in(INCOME.getKind(),
                    OUTCOME.getKind()),
                book.bookKey.eq(bookKey)
            )
            .groupBy(bookLine.lineDate, bookLineCategory.name)
            .fetch();
    }


}

