package com.example.petree.domain.matching.repository;

import com.example.petree.domain.matching.domain.Pledge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PledgeRepository extends JpaRepository<Pledge, Long> {
}
