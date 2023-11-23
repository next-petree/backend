package com.example.petree.domain.dog.repository;

import com.example.petree.domain.dog.domain.Dog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * packageName    : com.example.petree.domain.dog.repository
 * fileName       : DogRepository
 * author         : jsc
 * date           : 2023/07/04
 * description    : Dog Repository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/04        jsc                검색 조건에 따른 쿼리메소드 추가
 */
@Repository
public interface DogRepository extends JpaRepository<Dog, Long>, JpaSpecificationExecutor<Dog> {

    Page<Dog> findAll(Pageable pageable);

    Optional<Dog> findById(Long id);
    void delete(Dog dog);
}
