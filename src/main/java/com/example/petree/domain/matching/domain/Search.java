package com.example.petree.domain.matching.domain;

import com.example.petree.domain.adopter.domain.SearchType;
import lombok.Data;
import lombok.ToString;

/**
 * packageName    : com.example.petree.domain.matching.domain
 * fileName       : Search
 * author         : qkrtn_ulqpbq2
 * date           : 2023-09-12
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-12        qkrtn_ulqpbq2       최초 생성
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
