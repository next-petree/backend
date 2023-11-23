package com.example.petree.global.error.exception;

/**
 * packageName    : com.example.petree.global.error.exception
 * fileName       : IncorrectPasswordException
 * author         : 박수현
 * date           : 2023-08-24
 * description    : 비밀번호 변경 -> 새롭게 등록한 비밀번호와 기존비밀번호가 일치하지 않을때에 대한 예외처리
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-24        박수현              최초 생성
 */
public class IncorrectPasswordException extends RuntimeException{

    public IncorrectPasswordException() {
        super("기존 비밀번호와 일치하지 않습니다.");
    }
}
