package com.example.petree.domain.basic_test.repository;

import com.example.petree.domain.basic_test.domain.Choice;
import com.example.petree.domain.basic_test.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * packageName    : com.example.petree.domain.basic_test.repository
 * fileName       : ChoiceRepository
 * author         : 박수현
 * date           : 2023-08-05
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-05        박수현              최초 생성
 */

@Repository
public interface ChoiceRepository extends JpaRepository<Choice, Long> {
}
