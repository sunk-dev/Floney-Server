package com.floney.floney.book.service;

import com.floney.floney.book.dto.BookLineResponse;
import com.floney.floney.book.dto.CreateLineRequest;

public interface BookLineService {

    BookLineResponse addBookLine(String auth, CreateLineRequest request);
}
