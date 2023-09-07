package com.example.petree.global.error.exception;

import com.example.petree.global.error.ErrorCode;
import lombok.Getter;

/**
 * packageName    : com.example.petree.global.error.exception
 * fileName       : BusinessException
 * author         : 박수현
 * date           : 2023-07-11
 * description    : 비즈니스 로직 수행 중 예외를 발생시켜야하는 경우 사용할 Custom Exception 정의
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-11        박수현              최초 생성
 */

@Getter
public class BusinessException extends RuntimeException{
    private ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
