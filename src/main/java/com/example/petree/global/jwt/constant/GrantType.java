package com.example.petree.global.jwt.constant;

import lombok.Getter;

/**
 * packageName    : com.example.petree.global.jwt.constant
 * fileName       : GrantType
 * author         : qkrtn_ulqpbq2
 * date           : 2023-07-11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-11        qkrtn_ulqpbq2       최초 생성
 */

@Getter
public enum GrantType {
    BEARER("Bearer");

    GrantType(String type) {
        this.type = type;
    }

    private String type;
}
