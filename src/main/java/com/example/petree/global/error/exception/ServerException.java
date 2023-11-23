package com.example.petree.global.error.exception;

import com.example.petree.global.error.ErrorCode;
import lombok.Getter;

/**
 * packageName    : com.example.petree.global.error.exception
 * fileName       : NullPointerException
 * author         : jsc
 * date           : 2023/08/07
 * description    : 정세창
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/07         정세창              init
 */
@Getter
public class ServerException extends RuntimeException {

    private final ErrorCode errorCode;

    public ServerException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
