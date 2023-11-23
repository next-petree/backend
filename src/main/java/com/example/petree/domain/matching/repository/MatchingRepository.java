package com.example.petree.domain.matching.repository;

import com.example.petree.domain.breeder.domain.Breeder;
import com.example.petree.domain.matching.domain.Matching;
import com.example.petree.domain.adopter.domain.Adopter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchingRepository extends JpaRepository<Matching, Long>, JpaSpecificationExecutor<Matching> {

    Page<Matching> findByBreederOrderBySubmitDateDesc(Breeder breeder, Specification<Matching> spec,Pageable pageable);

    Page<Matching> findByAdopter(Adopter adopter, Specification<Matching> spec, Pageable pageable);
}
