package com.floney.floney.book;

import static com.floney.floney.fixture.BookFixture.BOOK_KEY;

import com.floney.floney.book.dto.request.CreateCategoryRequest;
import com.floney.floney.book.dto.response.CreateCategoryResponse;
import com.floney.floney.book.entity.Book;
import com.floney.floney.book.entity.BookLine;
import com.floney.floney.book.entity.BookLineCategory;
import com.floney.floney.book.entity.Category;
import com.floney.floney.book.entity.DefaultCategory;
import com.floney.floney.book.entity.RootCategory;
import com.floney.floney.book.entity.category.BookCategory;

public class CategoryFixture {

    public static final String ROOT = "부모 카테고리";
    public static final String CHILD = "추가하는 카테고리";

    public static CreateCategoryRequest createRootRequest() {
        return CreateCategoryRequest.builder()
            .bookKey(BOOK_KEY)
            .name(ROOT)
            .build();
    }

    public static RootCategory createDefaultRoot(String name) {
        return RootCategory.builder()
            .name(name)
            .build();
    }

    public static DefaultCategory createDefaultChild(Category parent, String name) {
        return DefaultCategory.builder()
            .parent(parent)
            .name(name)
            .build();
    }

    public static BookCategory createRootCategory(Book book) {
        return BookCategory.builder()
            .book(book)
            .name(ROOT)
            .build();
    }

    public static DefaultCategory incomeBookCategory() {
        return DefaultCategory.builder()
            .name("수입")
            .build();
    }

    public static DefaultCategory outComeBookCategory() {
        return DefaultCategory.builder()
            .name("지출")
            .build();
    }


    public static BookLineCategory createLineCategory(DefaultCategory category, BookLine bookLine) {
        return BookLineCategory.of(bookLine, category);
    }

    public static BookLineCategory createChildLineCategory(BookCategory category, BookLine bookLine) {
        return BookLineCategory.of(bookLine, category);
    }


    public static BookCategory createChildCategory(Category parent, Book book) {
        return BookCategory.builder()
            .book(book)
            .parent(parent)
            .name(CHILD)
            .build();
    }


    public static CreateCategoryRequest createBookCategory() {
        return CreateCategoryRequest.builder()
            .bookKey(BOOK_KEY)
            .parent(ROOT)
            .name(CHILD)
            .build();
    }

    public static CreateCategoryResponse categoryRootResponse() {
        return CreateCategoryResponse.builder()
            .name(ROOT)
            .build();
    }

    public static CreateCategoryResponse categoryChildResponse() {
        return CreateCategoryResponse.builder()
            .name(CHILD)
            .build();
    }

}
