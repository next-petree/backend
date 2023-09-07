--9번 문항
-- Question 데이터 삽입
INSERT INTO question (id, basic_test_id, question_text) VALUES (9, 1, '노쇠 증후군이 있는 개를 위한 일상관리에 해당하지 않는 것은?');

-- Choice 데이터 삽입
INSERT INTO choice (id, question_id, choice_text) VALUES (36, 9, '관절염이 있어 보행에 힘들어하는 경우 미끄럼 방지매트를 바닥 전체에 깔아준다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (37, 9, '근육 강화를 위해 강도 높은 스트레칭을 하는 것이 도움이 된다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (38, 9, '저지방, 저탄수화물, 고단백질의 식단을 관리한다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (39, 9, '걷기 너무 힘들어하면 1~2분 동안 가만히 서서 몸의 균형을 잡는 연습도 평형감각 유지에 도움이 된다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (40, 9, '신체에 맞는 놀이를 적극 고안해 낸다.');

-- Explanation 데이터 삽입
INSERT INTO explanation (id, question_id, explanation_text) VALUES (8, 9, '근육 강화를 위해 강도 높은 스트레칭은 노쇠 증후군이 있는 개에게는 무리를 줄 수 있습니다. 다른 선택지들은 노쇠 증후군 있는 개의 일상관리에 도움이 되는 방법입니다.');

--10번 문항
-- Question 데이터 삽입
INSERT INTO question (id, basic_test_id, question_text) VALUES (10, 1, '노령견의 관리법이 아닌 것은?');

-- Choice 데이터 삽입
INSERT INTO choice (id, question_id, choice_text) VALUES (41, 10, '활동량을 늘려 비만을 예방한다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (42, 10, '관절염이 있는 개에게는 온열매트로 체온을 유지하고 통증을 완화시켜 준다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (43, 10, '노령으로 활동량이 떨어진 개는 산책을 하지 않는 것이 좋다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (44, 10, '고개를 숙이기 힘들어지므로 밥그릇 높이를 높여 주는 것이 좋다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (45, 10, '움직임에 맞는 놀이를 지속한다.');

-- Explanation 데이터 삽입
INSERT INTO explanation (id, question_id, explanation_text) VALUES (9, 10, '노령으로 활동량이 떨어진 개에게는 오히려 산책을 하지 않는 것이 좋지 않습니다. 활동량을 유지하는 것이 노령견의 건강에 도움이 됩니다');

--11번 문항
-- Question 데이터 삽입
INSERT INTO question (id, basic_test_id, question_text) VALUES (11, 1, '다음 중 인지장애 증후군의 증상에 해당되지 않는 것은?');

-- Choice 데이터 삽입
INSERT INTO choice (id, question_id, choice_text) VALUES (46, 11, '문을 지나갈 때 열리는 쪽과 닫히는 쪽을 구분하지 못한다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (47, 11, '보호자에게 지나치게 의존적이며 함께 놀자고 계속 조른다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (48, 11, '밤에 이유 없이 짖는다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (49, 11, '대소변 실수가 잦아진다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (50, 11, '집이나 마당에서 길을 잃는다.');

-- Explanation 데이터 삽입
INSERT INTO explanation (id, question_id, explanation_text) VALUES (10, 11, '보호자에게 지나치게 의존적이며 함께 놀자고 계속 조르는 것은 인지장애 증후군의 증상 중 하나가 아닙니다. 다른 선택지들은 인지장애 증후군의 증상에 해당됩니다.');

--12번 문항
-- Question 데이터 삽입
INSERT INTO question (id, basic_test_id, question_text) VALUES (12, 1, '동물을 사별한 사람에 대한 대응으로 옳지 않은 것은?');

-- Choice 데이터 삽입
INSERT INTO choice (id, question_id, choice_text) VALUES (51, 12, '애도할 수 있는 충분한 시간을 준다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (52, 12, '잊으라고 강요하는 것은 펫로스 증후군을 심화시킬 수 있다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (53, 12, '가족 구성원의 사별과 동일한 위로와 정신적 지지가 필요하다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (54, 12, '심리적 고통이 심해서 일상생활에 어려움이 생길 경우 전문가의 도움을 받아야 한다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (55, 12, '비슷한 반려동물을 찾아 입양한다.');

-- Explanation 데이터 삽입
INSERT INTO explanation (id, question_id, explanation_text) VALUES (11, 12, '비슷한 반려동물을 찾아 입양하는 것은 펫로스 증후군을 심화시킬 수 있기 때문에 옳지 않은 대응 방법입니다. 애도할 수 있는 충분한 시간을 주고, 가족 구성원의 사별과 동일한 위로와 정신적 지지를 제공하며, 심리적 고통이 심하면 전문가의 도움을 받는 것이 좋습니다.');

