package com.example.petree.global.error.exception;

/**
 * packageName    : com.example.petree.global.error.exception
 * fileName       : MissingPrincipalException
 * author         : 박수현
 * date           : 2023-08-09
 * description    : principal파라미터 null처리
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-09        박수현              최초 생성
 */
public class MissingPrincipalException extends RuntimeException{

    public MissingPrincipalException() {
        super("로그인이 필요합니다.");
    }
}
