package com.example.petree.domain.adopter.repository;

import com.example.petree.domain.adopter.domain.Review;
import com.example.petree.domain.adopter.domain.SearchType;
import com.example.petree.domain.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * packageName    : com.example.petree.domain.adopter.repository
 * fileName       : ReviewRepositoryCustom
 * author         : 박수현
 * date           : 2023-08-29
 * description    : QueryDSL관련 인터페이스
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-29        박수현              최초 생성
 */
public interface ReviewRepositoryCustom {

    Page<Review> findReviewsByMember(Member member, SearchType searchType, String keyword, Pageable pageable);
}
