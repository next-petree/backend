package com.example.petree.domain.basic_test.repository;

import com.example.petree.domain.basic_test.domain.BasicTest;
import com.example.petree.domain.basic_test.domain.Explanation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * packageName    : com.example.petree.domain.basic_test.repository
 * fileName       : BasicTestRepository
 * author         : 박수현
 * date           : 2023-07-30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-30        박수현              최초 생성
 */

@Repository
public interface BasicTestRepository extends JpaRepository<BasicTest, Long> {

    /**
     * @author 박수현
     * @date 2023-08-15
     * @description : 클라이언트가 요청한 문항번호와 문항에 따른 선택지 번호를 토대로 정답 유무 판별
     * @return
     */

    @Query("SELECT c.isCorrect FROM Choice c WHERE c.question.id = :questionId AND c.id = :choiceId")
    Boolean calculateScore(Long questionId, Long choiceId);


    /**
     * @author 박수현
     * @date 2023-08-15
     * @description : 문항 번호에 따른 해설반환
     * @return
     */

    @Query("SELECT e FROM Explanation e WHERE e.question.id IN :questionIds")
    List<Explanation> getExplanationsForQuestions(List<Long> questionIds);

    /**
     * @author 박수현
     * @date 2023-08-15
     * @description : 5개의 선택지 중 정답의 id반환
     * @return
     */

    @Query("SELECT c.id FROM Choice c WHERE c.question.id = :questionId AND c.isCorrect = true")
    Long getCorrectChoiceId(@Param("questionId") Long questionId);
}
