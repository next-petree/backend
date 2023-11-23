package com.example.petree.domain.main_breed.repository;

import com.example.petree.domain.breeder.domain.Breeder;
import com.example.petree.domain.main_breed.domain.DogType;
import com.example.petree.domain.main_breed.domain.MainBreed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * packageName    : com.example.petree.domain.main_breed.repository
 * fileName       : breedTypeRepository
 * author         : 박수현
 * date           : 2023/08/11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/11        박수현              init
 */
@Repository
public interface MainBreedRepository extends JpaRepository<MainBreed, Long> {


    /**
     * @author 박수현
     * @date 2023-08-14
     * @description : 브리더id와 견종id에 따른 주력견종 조회 시 boolean타입으로 반환
     * @return
     */
    boolean existsByBreederAndDogType(Breeder breeder, DogType dogType);


    /**
     * @author 박수현
     * @date 2023-08-14
     * @description : 브리더id에 따라 전체 주력견종정보 삭제
     * @return
     */
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM main_breed WHERE breeder_id = :breederId", nativeQuery = true)
    void deleteByBreederId(@Param("breederId") Long breederId);

    /**
     * @author 박수현
     * @date 2023-08-14
     * @description : 브리더id와 견종id에 따른 주력견종 조회
     * @return
     */

    @Query(value = "SELECT * FROM main_breed WHERE breeder_id = :breederId AND dog_type_id = :dogTypeId", nativeQuery = true)
    MainBreed findByBreederAndDogType(@Param("breederId") Long breederId, @Param("dogTypeId") Long dogTypeId);

    /**
     * @author 박수현
     * @date 2023-07-15
     * @description : 로그인한 사용자에 따른 주력견종 목록 반환
     * @return List<MainBreed>
     */

    @Query(value = "SELECT * " +
            "FROM main_breed " +
            "WHERE breeder_id = :breederId",
            nativeQuery = true)
    List<MainBreed> findByBreederId(Long breederId);

    /**
     * @author 박수현
     * @date 2023-08-02
     * @description : 로그인한 사용자에 따른 주력견종 단일 데이터 반환
     * @return
     */
    @Query(value = "SELECT * " +
            "FROM main_breed mb " +
            "WHERE mb.breeder_id = :breederId AND mb.id = :id", nativeQuery = true)
    MainBreed findByBreederIdAndId(Long breederId, Long id);


    /**
     * @author 박수현
     * @date 2023-08-02
     * @description : main_breed테이블의 id값에 해당하는 주력견종 글 삭제
     * @return void
     */

    @Modifying
    @Query(value = "DELETE FROM main_breed WHERE id = :id", nativeQuery = true)
    void deleteById(Long id);
}
