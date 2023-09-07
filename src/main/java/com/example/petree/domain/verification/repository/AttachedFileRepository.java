package com.example.petree.domain.verification.repository;

import com.example.petree.domain.verification.domain.AttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachedFileRepository extends JpaRepository<AttachedFile, Long> {
}
