package com.example.petree.domain.adopter.domain;

import com.example.petree.domain.breeder.domain.Verification;
import lombok.Data;
import lombok.ToString;

/**
 * packageName    : com.example.petree.domain.adopter.domain
 * fileName       : SearchType
 * author         : qkrtn_ulqpbq2
 * date           : 2023-08-31
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-31        qkrtn_ulqpbq2       최초 생성
 */

@Data
@ToString
public class Search {

    private SearchType searchType;
    private String keyword;

    public boolean isEmpty() {
        return searchType == null || keyword == null;
    }
}
