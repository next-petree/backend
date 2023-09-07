package com.example.petree.domain.main_breed.repository;

import com.example.petree.domain.main_breed.domain.DogType;
import com.example.petree.domain.main_breed.domain.MainBreed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.List;
import java.util.Optional;

/**
 * packageName    : com.example.petree.domain.main_breed.repository
 * fileName       : DogTypeRepository
 * author         : 박수현
 * date           : 2023-08-14
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-14        박수현              최초 생성
 */

@Repository
public interface DogTypeRepository extends JpaRepository<DogType, Long> {

    List<DogType> findByNameContainingIgnoreCase(String keyword);

    Optional<DogType> findByName(String name);
}
