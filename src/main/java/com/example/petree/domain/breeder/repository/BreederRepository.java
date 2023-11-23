package com.example.petree.domain.breeder.repository;

import com.example.petree.domain.breeder.domain.Breeder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BreederRepository extends JpaRepository<Breeder, Long>, BreederRepositoryCustom{
    Optional<Breeder> findByEmail(String email);

    /**
     * packageName    : com.example.petree.global
     * fileName       : BreederRepository
     * author         : 박수현
     * date           : 2023-07-04
     * description    : 로그인한 사용자와 모든 브리더들의 거리를 계산하여 거리순으로 정렬. 이때 거리의 단위는 km.
     * ===========================================================
     * DATE              AUTHOR             NOTE
     * -----------------------------------------------------------
     * 2023-07-04        박수현              최초 생성
     */
    /*@Query(value = "SELECT m.id, " +git
            "m.nickname, " +
            "m.phone_number, " +
            "m.address, " +
            "m.latitude, " +
            "m.longitude, " +
            "b.is_verified, " +
            "GROUP_CONCAT(DISTINCT mb.type SEPARATOR ',') AS types, " +
            "(6371 * ACOS(COS(RADIANS(:memberLatitude)) * COS(RADIANS(m.latitude)) * COS(RADIANS(m.longitude) - RADIANS(:memberLongitude)) + SIN(RADIANS(:memberLatitude)) * SIN(RADIANS(m.latitude)))) " +
            "AS distance " +
            "FROM MEMBER m " +
            "JOIN BREEDER b ON m.id = b.id " +
            "LEFT JOIN MAIN_BREED mb ON b.id = mb.breeder_id " +
            "WHERE m.role = 'BREEDER' " +
            "GROUP BY m.id " +
            "ORDER BY distance ASC",
            countQuery = "SELECT COUNT(DISTINCT m.id) FROM MEMBER m JOIN BREEDER b ON m.id = b.id LEFT JOIN MAIN_BREED mb ON b.id = mb.breeder_id WHERE m.role = 'BREEDER'",
            nativeQuery = true)
    Page<BreederProjection> findNearbyBreedersOrderByDistance(@Param("memberLatitude") double memberLatitude,
                                                              @Param("memberLongitude") double memberLongitude,
                                                              Pageable pageable); */

    /**
     * packageName    : com.example.petree.global
     * fileName       : BreederRepository
     * author         : 박수현
     * date           : 2023-07-05
     * description    : 로그인한 사용자가 주력견종 키워드를 입력한 경우,
     *                  주력견종 키워드를 토대로 목록 반환, 이때, 브리더들의 거리를 계산하여 거리순으로 정렬
     *
     * ===========================================================
     * DATE              AUTHOR             NOTE
     * -----------------------------------------------------------
     * 2023-07-04        박수현              최초 생성
     */

    /* @Query(value = "SELECT m.id, " +
            "m.email, " +
            "m.nickname, " +
            "m.phone_number, " +
            "m.address, " +
            "m.latitude, " +
            "m.longitude, " +
            "b.is_verified, " +
            "GROUP_CONCAT(DISTINCT mb.type SEPARATOR ',') AS types, " +
            "(6371 * ACOS(COS(RADIANS(:memberLatitude)) * COS(RADIANS(m.latitude)) * COS(RADIANS(m.longitude) - RADIANS(:memberLongitude)) + SIN(RADIANS(:memberLatitude)) * SIN(RADIANS(m.latitude)))) " +
            "AS distance " +
            "FROM MEMBER m " +
            "JOIN BREEDER b ON m.id = b.id " +
            "LEFT JOIN MAIN_BREED mb ON b.id = mb.breeder_id " +
            "WHERE m.role = 'BREEDER' " +
            "AND mb.type LIKE %:breedKeyword% " +
            "GROUP BY m.id " +
            "ORDER BY distance ASC",
            countQuery = "SELECT COUNT(DISTINCT m.id) FROM MEMBER m JOIN BREEDER b ON m.id = b.id LEFT JOIN MAIN_BREED mb ON b.id = mb.breeder_id WHERE m.role = 'BREEDER' AND mb.type LIKE %:breedKeyword%", // 추가: 주력견종 키워드 필터링
            nativeQuery = true)
    Page<BreederProjection> findNearbyBreedersOrderByDistance(@Param("memberLatitude") double memberLatitude,
                                                              @Param("memberLongitude") double memberLongitude,
                                                              @Param("breedKeyword") String breedKeyword,
                                                              Pageable pageable); */
    /**
     * packageName    : com.example.petree.global
     * fileName       : BreederRepository
     * author         : 박수현
     * date           : 2023-07-05
     * description    : 로그인하지 않은 사용자가 주력견종키워드를 입력하였다면,
     *                  주력견종 키워드를 토대로 목록 반환,
     *                  만약 그렇지 않다면, 모든 데이터 반환
     *
     * ===========================================================
     * DATE              AUTHOR             NOTE
     * -----------------------------------------------------------
     * 2023-07-04        박수현              최초 생성
     */

    /* @Query(value = "SELECT m.id, " +
            "m.email, " +
            "m.nickname, " +
            "m.phone_number, " +
            "m.address, " +
            "m.latitude, " +
            "m.longitude, " +
            "b.is_verified, " +
            "GROUP_CONCAT(DISTINCT mb.type SEPARATOR ',') AS types " +
            "FROM MEMBER m " +
            "JOIN BREEDER b ON m.id = b.id " +
            "LEFT JOIN MAIN_BREED mb ON b.id = mb.breeder_id " +
            "WHERE m.role = 'BREEDER' " +
            "AND (CONCAT(',', mb.type, ',') LIKE CONCAT('%', :breedKeyword, '%') OR :breedKeyword IS NULL) " +
            "GROUP BY m.id",
            countQuery = "SELECT COUNT(DISTINCT m.id) FROM MEMBER m JOIN BREEDER b ON m.id = b.id LEFT JOIN MAIN_BREED mb ON b.id = mb.breeder_id WHERE m.role = 'BREEDER' AND (CONCAT(',', mb.type, ',') LIKE CONCAT('%', :breedKeyword, '%') OR :breedKeyword IS NULL)",
            nativeQuery = true)
    Page<BreederProjection> findBreedersByBreedKeyword(@Param("breedKeyword") String breedKeyword, Pageable pageable); */

}
