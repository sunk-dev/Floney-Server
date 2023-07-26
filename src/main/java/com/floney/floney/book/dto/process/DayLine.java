package com.floney.floney.book.dto.process;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class DayLine {

    private Long id;

    private String content;

    private Long money;

    private String categories;

    private String userNickName;

    public DayLine(Long id, String content, Long money, String categories) {
        this.id = id;
        this.content = content;
        this.money = money;
        this.categories = categories;
    }

    @QueryProjection
    public DayLine(Long id, String content, Long money, String categories, String userNickName) {
        this.id = id;
        this.content = content;
        this.money = money;
        this.categories = categories;
        this.userNickName = userNickName;
    }

}