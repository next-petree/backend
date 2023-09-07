package com.example.petree.domain.adopter.repository;

import com.example.petree.domain.adopter.domain.*;
import com.example.petree.domain.dog.domain.Gender;
import com.example.petree.domain.dog.domain.QDog;
import com.example.petree.domain.main_breed.domain.QDogType;
import com.example.petree.domain.member.domain.Member;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * packageName    : com.example.petree.domain.adopter.repository
 * fileName       : ReviewRepositoryImpl
 * author         : 박수현
 * date           : 2023-08-29
 * description    : QueryDSL 관련 클래스
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-29        박수현              최초 생성
 */

@Repository
@Slf4j
public class ReviewRepositoryImpl implements ReviewRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Review> findReviewsByMember(Member member, SearchType searchType, String keyword, Pageable pageable) {
        QReview review = QReview.review;
        QDog dog = QDog.dog;
        QDogType dogType = QDogType.dogType;
        QReviewImgFile reviewImgFile = QReviewImgFile.reviewImgFile;

        BooleanExpression memberExpression = review.adopter.eq((Adopter)member);
        log.info("searchType : " + searchType);
        BooleanExpression searchExpression = createSearchExpression(searchType, keyword);

        List<Review> results = queryFactory
                .selectDistinct(review)
                .from(review)
                .leftJoin(review.dog, dog)
                .leftJoin(dog.dogType, dogType)
                .where(memberExpression.and(searchExpression))
                .orderBy(review.writeDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory
                .selectDistinct(review)
                .from(review)
                .leftJoin(review.dog, dog)
                .leftJoin(dog.dogType, dogType)
                .where(memberExpression.and(searchExpression))
                .fetchCount();

        return new PageImpl<>(results, pageable, totalCount);
    }

    private BooleanExpression createSearchExpression(SearchType searchType, String keyword) {
        if (searchType == null || keyword == null) {
            return null;
        }

        QDog dog = QDog.dog;
        QDogType dogType = QDogType.dogType;
        QReview review = QReview.review;

        switch (searchType) {
            case name:
                log.info("이름 탔음");
                return dog.name.containsIgnoreCase(keyword);
            /*case "성별":
                return dog.gender.eq(Gender.valueOf(keyword));*/
            case type:
                log.info("견종 탔음");
                return dog.dogType.name.containsIgnoreCase(keyword);
            case content:
                log.info("제목 탔음");
                return review.title.containsIgnoreCase(keyword);
            default:
                return null;
        }
    }
}
