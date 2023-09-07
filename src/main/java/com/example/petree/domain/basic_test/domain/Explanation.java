package com.example.petree.domain.basic_test.domain;

import lombok.*;

import javax.persistence.*;

/**
 * packageName    : com.example.petree.domain.basic_test.domain
 * fileName       : Explanation
 * author         : 박수현
 * date           : 2023-07-30
 * description    : 문항과 해설은 서로 일대일 관계로,
 *                  OneToOne속성 대신에 @JoinColum에 unquie = true속성을 지정하여서
 *                  각 해설은 오직 하나의 문항과만 연결될 수 있도록 구현함.
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-30        박수현              최초 생성
 */

@Entity
@Table(name = "explanation")
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Explanation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id", unique = true)
    private Question question;

    private String explanationText;
}
