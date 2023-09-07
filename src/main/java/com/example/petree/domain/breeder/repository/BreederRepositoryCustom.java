package com.example.petree.domain.breeder.repository;

import com.example.petree.domain.breeder.domain.BreederProjection;
import com.example.petree.domain.breeder.domain.BreederProjectionImpl;
import com.example.petree.domain.breeder.domain.BreederSearch;
import com.example.petree.domain.breeder.dto.BreederDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * packageName    : com.example.petree.domain.breeder.repository
 * fileName       : BreederRepositoryCustom
 * author         : 박수현
 * date           : 2023-07-13
 * description    : BreederRepository를 상속받는 인터페이스로,
 *                  QueryDSL을 사용하기 위하여 구현체를 직접 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-13        박수현              최초 생성
 */
public interface BreederRepositoryCustom {
    Page<BreederProjectionImpl> findNearbyBreedersOrderByDistance(double memberLatitude, double memberLongitude, BreederSearch breederSearch, Pageable pageable);

    Page<BreederProjectionImpl> findBreeders(BreederSearch breederSearch, Pageable pageable);
}
