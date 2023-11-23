package com.example.petree.domain.matching.repository;

import com.example.petree.domain.matching.domain.MatchingApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchingApprovalRepository extends JpaRepository<MatchingApproval, Long> {
}
