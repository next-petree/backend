package com.example.petree.domain.adopter.domain;

import com.example.petree.domain.breeder.domain.Verification;
import lombok.Data;
import lombok.ToString;

/**
 * packageName    : com.example.petree.domain.adopter.domain
 * fileName       : ReviewSearch
 * author         : 박수현
 * date           : 2023-08-29
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-29        박수현              최초 생성
 */
@Data
@ToString
public class ReviewSearch {

    private Verification verification;
    private String keyword;

    public boolean isEmpty() {
        return verification == null || keyword == null;
    }
}
