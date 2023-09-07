package com.example.petree.domain.breeder.domain;

import lombok.Data;
import lombok.ToString;

/**
 * packageName    : com.example.petree.domain.breeder.domain
 * fileName       : BreederSearch
 * author         : qkrtn_ulqpbq2
 * date           : 2023-07-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-13        qkrtn_ulqpbq2       최초 생성
 */

@Data
@ToString
public class BreederSearch {
    private Verification verification;
    private String keyword;

    public boolean isEmpty() {
        return verification == null || keyword == null;
    }
}
