package com.example.petree.domain.basic_test.domain;

import lombok.*;

import javax.persistence.*;

/**
 * packageName    : com.example.petree.domain.basic_test.domain
 * fileName       : Choice
 * author         : 박수현
 * date           : 2023-07-30
 * description    : 기초지식테스트이 각 문항에 따른 선택지
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-30        박수현              최초 생성
 */

@Table(name = "choice")
@Entity
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Choice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    private String choiceText;

    private boolean isCorrect;
}
