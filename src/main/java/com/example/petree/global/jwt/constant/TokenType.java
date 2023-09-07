package com.example.petree.global.jwt.constant;

/**
 * packageName    : com.example.petree.global.jwt.constant
 * fileName       : TokenType
 * author         : qkrtn_ulqpbq2
 * date           : 2023-07-11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-11        qkrtn_ulqpbq2       최초 생성
 */
public enum TokenType {

    ACCESS, REFRESH;

    public static boolean isAccessToken(String tokenType) {
        return TokenType.ACCESS.name().equals(tokenType);
    }
}
