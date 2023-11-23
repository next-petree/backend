package com.example.petree.domain.adopter.repository;

import com.example.petree.domain.adopter.domain.ReviewImgFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * packageName    : com.example.petree.domain.adopter.repository
 * fileName       : ReviewImgFileRepository
 * author         : 박수현
 * date           : 2023-08-28
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-28        박수현              최초 생성
 */

@Repository
public interface ReviewImgFileRepository extends JpaRepository<ReviewImgFile, Long> {
}
