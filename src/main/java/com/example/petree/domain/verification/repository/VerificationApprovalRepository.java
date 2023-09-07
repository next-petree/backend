package com.example.petree.domain.verification.repository;

import com.example.petree.domain.verification.domain.VerificationApproval;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationApprovalRepository extends JpaRepository<VerificationApproval, Long> {
}
