package com.example.petree.domain.breeder.repository;

import com.example.petree.domain.breeder.domain.*;
import com.example.petree.domain.main_breed.domain.DogType;
import com.example.petree.domain.main_breed.domain.QDogType;
import com.example.petree.domain.main_breed.domain.QMainBreed;
import com.example.petree.domain.main_breed.repository.DogTypeRepository;
import com.example.petree.domain.member.domain.QMember;
import com.example.petree.domain.member.domain.Role;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.function.Predicate;


@Repository
@Slf4j
public class BreederRepositoryImpl implements BreederRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public BreederRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }


    @Override
    public Page<BreederProjectionImpl> findNearbyBreedersOrderByDistance(double memberLatitude, double memberLongitude, BreederSearch breederSearch, Pageable pageable) {
        QMember qMember = QMember.member;
        QBreeder qBreeder = QBreeder.breeder;
        QMainBreed qMainBreed = QMainBreed.mainBreed;
        QDogType qDogType = QDogType.dogType;

        NumberExpression<Double> distance = Expressions.numberTemplate(
                Double.class,
                "6371 * ACOS(COS(RADIANS({0})) * COS(RADIANS({1}.latitude)) * COS(RADIANS({1}.longitude) - RADIANS({2})) + SIN(RADIANS({0})) * SIN(RADIANS({1}.latitude)))",
                memberLatitude,
                qMember,
                memberLongitude
        );

        JPAQuery<BreederProjectionImpl> query = queryFactory
                .select(
                        Projections.constructor(
                                BreederProjectionImpl.class,
                                qMember.id,
                                qMember.email,
                                qMember.nickname,
                                qMember.phoneNumber,
                                qMember.address1,
                                qBreeder.isVerified,
                                distance.as("distance"),
                                Expressions.cases().when(qDogType.id.isNull()).then("").otherwise(qDogType.name)
                        )
                )
                .from(qMember)
                .join(qBreeder).on(qMember.id.eq(qBreeder.id))
                .leftJoin(qMainBreed).on(qBreeder.eq(qMainBreed.breeder))
                .leftJoin(qDogType).on(qMainBreed.dogType.eq(qDogType))
                .where(qMember.role.eq(Role.BREEDER));

        BooleanBuilder conditions = new BooleanBuilder();

        if (breederSearch.getVerification() == Verification.yes) {
            log.info("인증된 브리더 요청");
            conditions.and(qBreeder.isVerified.eq(true));
        } else if (breederSearch.getVerification() == Verification.no) {
            log.info("인증되지 않은 브리더 요청");
            conditions.and(qBreeder.isVerified.eq(false));
        }

        String keyword = breederSearch.getKeyword();
        if (keyword != null && !keyword.isEmpty()) {
            log.info("주력견종키워드 검색");
            //conditions.and(qMainBreed.dogType.name.like("%" + keyword + "%"));
            // 키워드가 주력견종 키워드에 해당하는 브리더들을 검색
            conditions.and(qBreeder.id.in(
                    JPAExpressions.selectDistinct(qMainBreed.breeder.id)
                            .from(qMainBreed)
                            .where(qMainBreed.dogType.name.like("%" + keyword + "%"))
            ));
        }

        query.where(conditions)
                .orderBy(distance.asc());

        log.info("쿼리 실행 완료");
        log.info("요청한 검색 정보: " + breederSearch);


        List<BreederProjectionImpl> content = query.fetch();

        long totalCount = query.fetchCount();

        log.info("content : " + content);

        return new PageImpl<>(content, pageable, totalCount);
    }

    @Override
    public Page<BreederProjectionImpl> findBreeders(BreederSearch breederSearch, Pageable pageable) {

        QMember qMember = QMember.member;
        QBreeder qBreeder = QBreeder.breeder;
        QMainBreed qMainBreed = QMainBreed.mainBreed;
        QDogType qDogType = QDogType.dogType;

        JPAQuery<BreederProjectionImpl> query = queryFactory
                .select(
                        Projections.constructor(
                                BreederProjectionImpl.class,
                                qMember.id,
                                qMember.email,
                                qMember.nickname,
                                qMember.phoneNumber,
                                qMember.address1,
                                qBreeder.isVerified,
                                Expressions.cases().when(qDogType.id.isNull()).then("").otherwise(qDogType.name)
                        )
                )
                .from(qMember)
                .join(qBreeder).on(qMember.id.eq(qBreeder.id))
                .leftJoin(qMainBreed).on(qBreeder.eq(qMainBreed.breeder))
                .leftJoin(qDogType).on(qMainBreed.dogType.eq(qDogType))
                .where(qMember.role.eq(Role.BREEDER));

        BooleanBuilder conditions = new BooleanBuilder();
        if (breederSearch.getVerification() == Verification.yes) {
            log.info("인증된 브리더 요청");
            conditions.and(qBreeder.isVerified.eq(true));
        } /*else if (breederSearch.getVerification() == Verification.no) {
            log.info("인증되지 않은 브리더 요청");
            conditions.and(qBreeder.isVerified.eq(false));
        }*/

        String keyword = breederSearch.getKeyword();
        if (keyword != null && !keyword.isEmpty()) {
            log.info("주력견종키워드 검색");
            //conditions.and(qMainBreed.dogType.name.like("%" + keyword + "%"));
            // 키워드가 주력견종 키워드에 해당하는 브리더들을 검색
            conditions.and(qBreeder.id.in(
                    JPAExpressions.selectDistinct(qMainBreed.breeder.id)
                            .from(qMainBreed)
                            .where(qMainBreed.dogType.name.like("%" + keyword + "%"))
            ));
        }

        query.where(conditions).orderBy(qBreeder.id.asc());

        log.info("쿼리 실행 완료");
        log.info("요청한 검색 정보: " + breederSearch);

        List<BreederProjectionImpl> content = query.fetch();
        long totalCount = query.fetchCount();

        log.info("content : " + content);

        return new PageImpl<>(content, pageable, totalCount);

    }

}