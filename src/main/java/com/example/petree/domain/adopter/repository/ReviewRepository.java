package com.example.petree.domain.adopter.repository;

import com.example.petree.domain.adopter.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * packageName    : com.example.petree.domain.adopter.repository
 * fileName       : ReviewRepository
 * author         : qkrtn_ulqpbq2
 * date           : 2023-08-28
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-28        qkrtn_ulqpbq2       최초 생성
 */

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {
    @Transactional
    @Modifying
    @Query("DELETE FROM ReviewImgFile WHERE review = :review")
    void deleteReviewImgFilesByReview(@Param("review") Review review);
}
