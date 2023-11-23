package com.example.petree.domain.basic_test.service;

import com.example.petree.domain.basic_test.domain.*;
import com.example.petree.domain.basic_test.dto.*;
import com.example.petree.domain.basic_test.repository.BasicTestRepository;
import com.example.petree.domain.basic_test.repository.ChoiceRepository;
import com.example.petree.domain.basic_test.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.example.petree.domain.basic_test.dto.ExplanationDto.createExplanataionDto;

/**
 * packageName    : com.example.petree.domain.basic_test.service
 * fileName       : BasicService
 * author         : 박수현
 * date           : 2023-07-30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-30        박수현              최초 생성
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicTestService {

    private final BasicTestRepository basicTestRepository;
    private final QuestionRepository questionRepository;
    private final ChoiceRepository choiceRepository;

    /**
     * @author 박수현
     * @date 2023-08-05
     * @description : 기초지식테스트 조회(문항 랜덤배치 및 문항에 따른 선택지 역시 랜덤배치)
     * @return BasicTestDto
     */

    @Transactional
    public BasicTestDto startBasicTest() {
        List<Question> allQuestions = questionRepository.findAll();
        List<Choice> allChoices = choiceRepository.findAll();

        // 문항과 선택지를 랜덤하게 배치
        Collections.shuffle(allQuestions);
        Collections.shuffle(allChoices);

        // 82개의 문제 중에서 12문제 선택
        List<Question> selectedQuestions = allQuestions.subList(0, 12);

        // 반환할 DTO 객체를 초기화
        BasicTestDto basicTestDto = new BasicTestDto();
        List<QuestionDto> questionDtos = new ArrayList<>();

        // 랜덤하게 선택된 12개의 문제에 대해 진행
        for (Question question : selectedQuestions) {
            QuestionDto questionDto = new QuestionDto();
            questionDto.setId(question.getId());
            questionDto.setQuestionText(question.getQuestionText());

            // 문항에 해당하는 선택지들 중에서 5개 랜덤하게 선택
            List<Choice> choicesForQuestion = getChoicesForQuestion(allChoices, question);
            Collections.shuffle(choicesForQuestion);
            List<Choice> selectedChoices = choicesForQuestion.subList(0, 5);

            List<ChoiceDto> choiceDtos = new ArrayList<>();
            for (Choice choice : selectedChoices) {
                ChoiceDto choiceDto = new ChoiceDto();
                choiceDto.setId(choice.getId());
                choiceDto.setChoiceText(choice.getChoiceText());
                choiceDtos.add(choiceDto);
            }

            // 선택지들을 랜덤하게 배치
            Collections.shuffle(choiceDtos);

            questionDto.setChoices(choiceDtos);
            questionDtos.add(questionDto);
        }

        // 문항들을 다시 랜덤하게 섞기
        Collections.shuffle(questionDtos);

        basicTestDto.setQuestions(questionDtos);

        return basicTestDto;
    }

    /**
     * @author 박수현
     * @date 2023-08-06
     * @description : 문항에 대한 선택지 반환
     * @return List<Choice>
     */

    private List<Choice> getChoicesForQuestion(List<Choice> allChoices, Question question) {
        List<Choice> choicesForQuestion = new ArrayList<>();
        for (Choice choice : allChoices) {
            if (choice.getQuestion().equals(question)) {
                choicesForQuestion.add(choice);
            }
        }
        return choicesForQuestion;
    }

    /**
     * @author 박수현
     * @date 2023-08-06
     * @description : 사용자가 제출한 문항 채점 및 틀린 문항에 대한 해설 반환
     * @return TestResultDto.TestResultResponseDto
     */

    public TestResultDto.TestResultResponseDto submitTestResult(TestResultDto.TestResultRequestDto requestDto) {
        List<TestAnswerDto> answers = requestDto.getAnswers();
        int totalScore = 0;
        List<Long> incorrectQuestionIds = new ArrayList<>();

        for (TestAnswerDto answer : answers) {
            Long questionId = answer.getQuestionId();
            Long selectedChoiceId = answer.getSelectedChoiceId();

            // 클라이언트가 보낸 문항 내용과 선택지 내용을 토대로 채점을 수행
            boolean isCorrect = basicTestRepository.calculateScore(questionId, selectedChoiceId);
            int score = isCorrect ? calculateSingleQuestionScore() : 0;
            log.info("점수 : " + score);
            totalScore += score;

            if (score == 0) {
                incorrectQuestionIds.add(questionId);
            }
        }

        log.info("틀린문항 : " + incorrectQuestionIds);

        if (incorrectQuestionIds.isEmpty()) {
            totalScore = 100;
        }

        boolean passed = totalScore >= 80;
        List<Explanation> explanations = basicTestRepository.getExplanationsForQuestions(incorrectQuestionIds);
        List<ExplanationDto> explanationDtos = new ArrayList<>();

        for (Explanation explanation : explanations) {
            // 실제 정답 번호를 가져오는 메서드를 사용하여 정답 번호를 얻음
            Long correctChoiceId = basicTestRepository.getCorrectChoiceId(explanation.getQuestion().getId());

            ExplanationDto explanationDto = createExplanataionDto(explanation, correctChoiceId);
            explanationDtos.add(explanationDto);
        }


        return TestResultDto.TestResultResponseDto.builder()
                .score(totalScore)
                .passed(passed)
                .explanations(explanationDtos)
                .build();
    }

    /**
     * @author 박수현
     * @date 2023-08-06
     * @description : 객관식 12문항 -> 100점만점으로 하였을때 한 문항당 점수 반환
     * @return int
     */

    private int calculateSingleQuestionScore() {
        // 각 문항당 점수 계산 로직 (100 / 12)
        return Math.round(100.0f / 12);
    }

}
