package com.floney.floney.common.exception.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorType {

    USER_FOUND("U001", "이미 존재하는 유저입니다"),
    NOT_AUTHENTICATED("U002", "로그인이 필요합니다"),
    INVALID_MAIL_ADDRESS("U003", "메일 주소가 올바르지 않습니다"),
    FAIL_TO_SEND_MAIL("U004", "메일 서버에 오류가 발생했습니다"),
    INVALID_AUTHENTICATION("U005", "올바르지 않은 인증 요청 입니다"),
    EXPIRED_JWT_TOKEN("U006", "만료된 토큰입니다"),
    INVALID_JWT_TOKEN("U007", "올바르지 않은 토큰입니다"),
    USER_NOT_FOUND("U008", "해당 이메일로 가입된 유저가 없습니다"),
    INVALID_LOGIN("U009", "잘못된 정보로 로그인에 실패했습니다"),
    USER_INACTIVE("U010", "휴면 회원입니다"),
    INVALID_PROVIDER("U011", "올바르지 않은 회원 유형입니다"),
    EMAIL_NOT_FOUND("U012", "이메일이 존재하지 않습니다"),
    INVALID_CODE("U013", "올바르지 않은 인증 코드입니다"),
    INVALID_EMAIL("U014", "올바르지 않은 이메일입니다"),
    INVALID_OAUTH_RESPONSE("U015", "인증서버에서 받은 응답이 올바르지 않습니다"),
    INVALID_OAUTH_TOKEN("U016", "provider 토큰이 올바르지 않습니다"),
    SAME_PASSWORD("U017", "이전 비밀번호와 같습니다"),
    CODE_NOT_FOUND("U018", "코드가 존재하지 않습니다"),
    EMPTY_SIGNOUT_OTHER_REASON("U019", "기타 탈퇴 사유가 없습니다"),
    SUBSCRIBE("U020", "구독자 입니다"),
    NOT_EMAIL_USER("U021", "이메일 유저가 아닙니다"),

    NOT_FOUND_BOOK("B001", "가계부가 존재하지 않습니다"),
    MAX_MEMBER("B002", "최대 인원이 초과되었습니다"),
    NOT_FOUND_CATEGORY("B003", "카테고리가 존재하지 않습니다"),
    NO_DELETE_BOOK("B004", "남은 가계부 유저가 있습니다"),
    NO_AUTHORITY("B005", "가계부를 만든 사람만 삭제할 수 있습니다"),
    NOT_FOUND_BOOK_USER("B006", "가계부 멤버를 찾을 수 없습니다"),
    NOT_FOUND_BOOK_LINE("B007", "가계부 내역을 찾을 수 없습니다"),
    ALREADY_JOIN("B008", "이미 존재하는 가계부 유저입니다"),
    FAIL_TO_CREATE_EXCEL("B009", "엑셀 파일을 생성하는 중 오류가 발생했습니다"),
    NOT_FOUND_ALARM("B010", "알람 내역을 찾을 수 없습니다"),

    LIMIT("S002", "제공하지 않는 서비스입니다"),

    NOT_FOUND_SETTLEMENT("ST001", "정산 내역이 존재하지 않습니다"),
    NOT_FOUND_OUTCOME_USER("ST002", "지출 내역의 유저가 유저 목록에 존재하지 않습니다"),

    FAIL_TO_GENERATE_TOKEN("A001", "외부 서버에서 토큰을 요청하는 중 에러가 발생했습니다"),

    SERVER_ERROR("0", "서버에 알 수 없는 에러가 발생했습니다"),
    REQUEST_BODY_ERROR("1", "요청 body의 내용이 올바르지 않습니다"),
    REQUEST_PARAMETER_ERROR("2", "요청 parameter가 올바르지 않습니다");

    private final String code;
    private final String message;
}

