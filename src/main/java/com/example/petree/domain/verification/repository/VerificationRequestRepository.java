package com.example.petree.domain.verification.repository;

import com.example.petree.domain.breeder.domain.Breeder;
import com.example.petree.domain.verification.domain.Certification;
import com.example.petree.domain.verification.domain.Status;
import com.example.petree.domain.verification.domain.VerificationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
public interface VerificationRequestRepository extends JpaRepository<VerificationRequest, Long>, JpaSpecificationExecutor<VerificationRequest> {

    VerificationRequest findByBreederAndCertification(Breeder breeder, Certification certification);


    /**
     * @author 이지수
     * @date 2023-08-08
     * @description : 브리더 인증 시, 승인이 1개 이상 있으면 인증된 브리더로 저장하기 위함
     * @return List<VerificationRequest>
     */
    List<VerificationRequest> findByBreederIdAndStatus(Long breederId, Status status);
    /**
     * @author 이지수
     * @date 2023-08-09
     * @description : 검색 조건
     * @return
     */
    Page<VerificationRequest> findAll(Specification<VerificationRequest> spec, Pageable pageable);
}
