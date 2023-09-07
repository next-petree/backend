--5번 문항
-- Question 데이터 삽입
INSERT INTO question (id, basic_test_id, question_text) VALUES (5, 1, '다음 중 신생아기의 특징이 아닌 것은?');

-- Choice 데이터 삽입
INSERT INTO choice (id, question_id, choice_text) VALUES (16, 5, '눈과 귀가 열리지 않은 채로 태어난다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (17, 5, '치아 발육이 시작되어 어미젖을 깨물기 시작한다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (18, 5, '스스로 배변과 배뇨가 불가능하여 어미나 보호자의 도움을 받아야 한다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (19, 5, '체온조절이 불가능하다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (20, 5, '따뜻한 곳을 스스로 찾아갈 수 있다.');

-- Explanation 데이터 삽입
INSERT INTO explanation (id, question_id, explanation_text) VALUES (4, 5, '신생아기는 체온조절이 불가능한 특징이 없습니다. 다른 선택지들은 모두 신생아기의 특징입니다.');

--6번 문항
-- Question 데이터 삽입
INSERT INTO question (id, basic_test_id, question_text) VALUES (6, 1, '다음 중 신생견의 수유에 대한 설명으로 옳은 것은?');

-- Choice 데이터 삽입
INSERT INTO choice (id, question_id, choice_text) VALUES (21, 6, '우유는 직접 가열하거나 전자레인지로 데우면 단백질이 파괴되고 유지방이 엉길 수 있으므로 차게 먹이는 것이 좋다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (22, 6, '분유는 정확한 조성으로 농도가 조절되어 있으므로 간편하게 수유할 수 있지만 보관하는 동안 세균이 번식할 수 있다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (23, 6, '액상우유는 보존이 용이하지만 우유의 농도 조절에 실패할 경우 설사의 원인이 되기도 하고 영양 부족이 올 수 있다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (24, 6, '먹고 남은 우유는 냉장 보관하였다가 다시 데워 사용한다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (25, 6, '고양이 우유를 개에게 먹여도 된다.');

-- Explanation 데이터 삽입
INSERT INTO explanation (id, question_id, explanation_text) VALUES (5, 6, '신생견의 수유에 대해서 옳은 설명은 "먹고 남은 우유는 냉장 보관하였다가 다시 데워 사용한다." 입니다. 다른 선택지들은 잘못된 설명입니다.');

--7번 문항
-- Question 데이터 삽입
INSERT INTO question (id, basic_test_id, question_text) VALUES (7, 1, '어린 강아지의 산책에 대한 설명 중 옳지 않은 것은?');

-- Choice 데이터 삽입
INSERT INTO choice (id, question_id, choice_text) VALUES (26, 7, '전염병 예방 차원에서 3차 접종 전까지는 팔에 안고 나가는 것이 좋다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (27, 7, '걸어서 하는 산책은 5차 종합백신 이후부터 하는 것이 좋다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (28, 7, '산책 준비물에는 인식표, 목줄, 배변용품을 반드시 가지고 나가야 한다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (29, 7, '내장형 무선식별장치를 했다면 인식표는 하지 않아도 된다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (30, 7, '산책은 사회화에 도움이 된다.');

-- Explanation 데이터 삽입
INSERT INTO explanation (id, question_id, explanation_text) VALUES (6, 7, '어린 강아지의 산책에 대해서 옳지 않은 설명은 "내장형 무선식별장치를 했다면 인식표는 하지 않아도 된다." 입니다. 반려견과 산책 시, 내장형 무선식별장치를 했어도 인식표를 착용해야 합니다.');

--8번 문항
-- Question 데이터 삽입
INSERT INTO question (id, basic_test_id, question_text) VALUES (8, 1, '화식사료와 생식사료에 대한 설명으로 옳지 않은 것은?');

-- Choice 데이터 삽입
INSERT INTO choice (id, question_id, choice_text) VALUES (31, 8, '화식사료란 불에 익힌 사료를 의미한다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (32, 8, '화식사료는 기호성이 매우 높고 소화 흡수율이 높다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (33, 8, '집에서 만들어주는 화식은 영양소를 골고루 고려할 수 있어서 가장 완벽한 식사로 인정할 수 있다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (34, 8, '화식사료, 생식사료 모두 알레르기 유발물질을 제외하고 급여하기에 용이하다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (35, 8, '생식사료는 원재료를 조리 없이 급여하는 것을 말한다.');

-- Explanation 데이터 삽입
INSERT INTO explanation (id, question_id, explanation_text) VALUES (7, 8, '집에서 만들어주는 화식은 영양소를 골고루 고려할 수 없어서 영양불균형이 올 수 있습니다. 다른 선택지들은 화식사료와 생식사료에 대한 옳은 설명입니다.');
