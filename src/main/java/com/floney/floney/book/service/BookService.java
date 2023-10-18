package com.floney.floney.book.service;

import com.floney.floney.book.dto.process.OurBookInfo;
import com.floney.floney.book.dto.request.BookNameChangeRequest;
import com.floney.floney.book.dto.request.BookUserOutRequest;
import com.floney.floney.book.dto.request.CarryOverRequest;
import com.floney.floney.book.dto.request.ChangeCurrencyRequest;
import com.floney.floney.book.dto.request.CodeJoinRequest;
import com.floney.floney.book.dto.request.CreateBookRequest;
import com.floney.floney.book.dto.request.SaveAlarmRequest;
import com.floney.floney.book.dto.request.SeeProfileRequest;
import com.floney.floney.book.dto.request.UpdateAlarmReceived;
import com.floney.floney.book.dto.request.UpdateAssetRequest;
import com.floney.floney.book.dto.request.UpdateBookImgRequest;
import com.floney.floney.book.dto.request.UpdateBudgetRequest;
import com.floney.floney.book.dto.response.BookInfoResponse;
import com.floney.floney.book.dto.response.BookStatusResponse;
import com.floney.floney.book.dto.response.BookUserResponse;
import com.floney.floney.book.dto.response.CreateBookResponse;
import com.floney.floney.book.dto.response.CurrencyResponse;
import com.floney.floney.book.dto.response.InviteCodeResponse;
import com.floney.floney.book.dto.response.InvolveBookResponse;
import com.floney.floney.book.dto.response.LastSettlementDateResponse;
import com.floney.floney.book.entity.Book;
import com.floney.floney.user.dto.response.AlarmResponse;
import com.floney.floney.user.dto.security.CustomUserDetails;
import com.floney.floney.user.entity.User;
import java.time.Month;
import java.util.List;
import java.util.Map;

public interface BookService {

    CreateBookResponse addBook(final User user, final CreateBookRequest request);

    CreateBookResponse joinWithCode(final CustomUserDetails userDetails, final CodeJoinRequest code);

    void changeBookName(final BookNameChangeRequest request);

    void deleteBook(final String email, final String bookKey);

    OurBookInfo getBookInfo(final String bookKey, final String myEmail);

    void updateBookImg(final UpdateBookImgRequest request);

    void updateSeeProfile(final SeeProfileRequest request);

    void updateCarryOver(final CarryOverRequest request);

    void saveOrUpdateAsset(final UpdateAssetRequest request);

    void saveOrUpdateBudget(final UpdateBudgetRequest request);

    InvolveBookResponse findInvolveBook(final User user);

    void bookUserOut(final BookUserOutRequest request, final String username);

    InviteCodeResponse inviteCode(final String bookKey);

    List<BookUserResponse> findUsersByBook(final CustomUserDetails userDetails, final String bookKey);

    CurrencyResponse changeCurrency(final ChangeCurrencyRequest request);

    Book makeInitBook(final String bookKey);

    CurrencyResponse getCurrency(String bookKey);

    BookInfoResponse getBookInfoByCode(String code);

    LastSettlementDateResponse getPassedDaysAfterLastSettlementDate(String userEmail, String bookKey);

    BookStatusResponse getBookStatus(String bookKey);

    Map<Month, Long> getBudgetByYear(String bookKey, String year);

    void saveAlarm(SaveAlarmRequest request);

    void updateAlarmReceived(UpdateAlarmReceived request);

    List<AlarmResponse> getAlarmByBook(String bookKey, String email);

    void leaveBooksBy(long userId);
}
