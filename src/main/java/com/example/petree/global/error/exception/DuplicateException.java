package com.example.petree.global.error.exception;

/**
 * packageName    : com.example.petree.global.error.exception
 * fileName       : DuplicateException
 * author         : 박수현
 * date           : 2023-08-21
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-21        박수현              최초 생성
 */
public class DuplicateException extends RuntimeException{

    public DuplicateException(String message) {
        super(message);
    }
}
