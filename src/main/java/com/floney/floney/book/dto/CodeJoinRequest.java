package com.floney.floney.book.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CodeJoinRequest {
    private String code;

    public CodeJoinRequest(String code) {
        this.code = code;
    }
}