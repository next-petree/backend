package com.example.petree.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * packageName    : com.example.petree.global.error
 * fileName       : ErrorCode
 * author         : 박수현
 * date           : 2023-07-11
 * description    : 반환할 http status 값, 에러 코드, 에러메세지를 관리하는 Enum 클래스
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-11        박수현              최초 생성
 */

@Getter
public enum ErrorCode {

    // 인증 && 인가
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A-001", "토큰이 만료되었습니다."),
    NOT_VALID_TOKEN(HttpStatus.UNAUTHORIZED, "A-002", "해당 토큰은 유효한 토큰이 아닙니다."),
    NOT_EXISTS_AUTHORIZATION(HttpStatus.UNAUTHORIZED, "A-003", "Authorization Header가 빈값입니다."),
    NOT_VALID_BEARER_GRANT_TYPE(HttpStatus.UNAUTHORIZED, "A-004", "인증 타입이 Bearer 타입이 아닙니다."),
    NOT_ACCESS_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "A-007", "해당 토큰은 ACCESS TOKEN이 아닙니다."),
    FORBIDDEN_ADMIN(HttpStatus.FORBIDDEN, "A-008", "관리자 Role이 아닙니다."),
    BLACKLISTED_ACCESS_TOKEN(HttpStatus.BAD_REQUEST,"A-009","ACCESS TOKEN이 만료되었습니다."),
    NOT_LOGIN(HttpStatus.BAD_REQUEST, "A-010", "로그인이 필요합니다."),

    // 회원
    INVALID_MEMBER_TYPE(HttpStatus.BAD_REQUEST, "M-001", "잘못된 회원 타입 입니다.(memberType : KAKAO)"),
    ALREADY_REGISTERED_MEMBER(HttpStatus.BAD_REQUEST, "M-002", "이미 가입된 회원 입니다."),
    MEMBER_NOT_EXISTS(HttpStatus.BAD_REQUEST, "M-003", "해당 회원은 존재하지 않습니다."),
    NOT_BREEDER(HttpStatus.BAD_REQUEST, "M-004", "브리더 회원이 아닙니다."),
    NOT_ADOPTER(HttpStatus.BAD_REQUEST, "M-005", "입양희망자 회원이 아닙니다."),

    //카카오 연동
    NOT_OAUTH_HISTORY(HttpStatus.BAD_REQUEST, "O-001", "카카오에 연동된 계정이 없습니다."),

    //요청 파라미터 관련
    INVALID_PARAM(HttpStatus.BAD_REQUEST, "P-001", "유효하지 않은 요청 파라미터가 존재합니다."),

    //엔티티 조회 시 Null일 때
    NULL_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "N-001", "엔티티가 조회되지 않았습니다.(NullPointer Exception)"),

    //주거환경 갱신 시 하나의 공간에 대해 2개의 이미지를 넣으려고 할 때
    HAS_MORE_THAN_ONE_SPACE_TYPE(HttpStatus.BAD_REQUEST, "E-001", "한 공간 타입에 대해서는 하나의 이미지만 지정할 수 있습니다.");

    ErrorCode(HttpStatus httpStatus, String errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }

    private HttpStatus httpStatus;
    private String errorCode;
    private String message;
}

