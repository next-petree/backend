-- BasicTest 테이블에 데이터 삽입
INSERT INTO basic_test (id) VALUES (1);

--1번 문항
-- Question 테이블에 데이터 삽입
INSERT INTO question (id, basic_test_id, question_text) VALUES (1, 1, '개를 크게 소형견, 중형견, 대형견으로 나눌 때 소형견에 해당하는 성견의 체중과 체고는 무엇인가?');

-- Choice 테이블에 데이터 삽입
INSERT INTO choice (id, question_id, choice_text) VALUES (1, 1, '체중 10Kg 이하, 체고 40cm 미만');
INSERT INTO choice (id, question_id, choice_text) VALUES (2, 1, '체중 5Kg 이하, 체고 40cm 미만');
INSERT INTO choice (id, question_id, choice_text) VALUES (3, 1, '체중 3Kg 이하, 체고 20cm 미만');
INSERT INTO choice (id, question_id, choice_text) VALUES (4, 1, '체중 8Kg 이하, 체고 20cm 미만');
INSERT INTO choice (id, question_id, choice_text) VALUES (5, 1, '체중 15Kg 이하, 체고 60cm 미만');

-- Explanation 테이블에 데이터 삽입
INSERT INTO explanation (id, question_id, explanation_text) VALUES (1, 1, '협회마다 차이는 조금씩 있지만 소형견은 몸무게 10kg 이하에 체고 40cm 이하의 개를 말한다. 체중 3Kg 이하일 경우 초소형견이라고 하기도 한다.');

--2번 문항
INSERT INTO question (id, basic_test_id, question_text) VALUES (2, 1, '개의 키를 말하는 체고는 어떻게 측정하는가?');

INSERT INTO choice (id, question_id, choice_text) VALUES (1, 2, '주둥이부터 엉덩이 뼈까지의 길이');
INSERT INTO choice (id, question_id, choice_text) VALUES (2, 2, '어깨뼈의 가장 윗부분(기갑)부터 앞발이 지면에 닿는 곳까지의 길이');
INSERT INTO choice (id, question_id, choice_text) VALUES (3, 2, '머리의 가장 윗부분인 두정부부터 앞발이 지면에 닿는 곳까지의 길이');
INSERT INTO choice (id, question_id, choice_text) VALUES (4, 2, '주둥이부터 꼬리 끝까지의 길이');
INSERT INTO choice (id, question_id, choice_text) VALUES (5, 2, '엉덩이 뼈의 가장 윗부분부터 뒷발이 지면에 닿는 곳까지의 길이');

INSERT INTO explanation (id, question_id, explanation_text) VALUES (2, 2, '체고는 어깨뼈의 가장 윗부분(기갑)부터 앞발이 지면에 닿는 곳까지의 길이를 잰다.');
--3번 문항
INSERT INTO question (id, basic_test_id, question_text) VALUES (3, 1, '다음 중 반려견 입양 시 적절하지 않은 자세는?');

INSERT INTO choice (id, question_id, choice_text) VALUES (6, 3, '가족 간의 회의를 거쳐 입양시기, 입양견의 종류 등을 결정한다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (7, 3, '가족 구성원 중 동물 알레르기가 있을 경우, 입양하지 않는 것이 좋다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (8, 3, '동물보호소에 갔더니 유난히 나를 따르는 개가 있을 경우 입양한다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (9, 3, '가족 구성원의 출퇴근 시간 등을 고려하여 입양한다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (10, 3, '선호하는 품종과 기를 수 있는 여건을 고려하여 입양해야 한다');

INSERT INTO explanation (id, question_id, explanation_text) VALUES (3, 3, '즉흥적인 입양은 쉽게 파양으로 이어진다. 입양 전 충분한 고민과 가족 구성원의 형편을 고려하는 것이 좋다.');

--4번 문항
INSERT INTO question (id, basic_test_id, question_text) VALUES (4, 1, '입양 후 돌보기 중 바르지 않은 것은?');

INSERT INTO choice (id, question_id, choice_text) VALUES (11, 4, '입양 당시 먹던 사료를 최소 일주일간은 지속적으로 먹인다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (12, 4, '입양 당시 먹던 사료량을 일주일간은 지속적으로 준다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (13, 4, '입양 당일은 집안 곳곳을 탐색하도록 하고 지나치게 만지지 않는다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (14, 4, '입양한 당일 더운물로 목욕을 하는 것이 좋다.');
INSERT INTO choice (id, question_id, choice_text) VALUES (15, 4, '화장실 교육은 입양 첫날에 실시할 수 있다.');

INSERT INTO explanation (id, question_id, explanation_text) VALUES (4, 4, '입양 당일은 많은 스트레스가 있으므로 목욕은 금하는 것이 좋다. 오염된 곳이 있다면 부분적으로 닦는다.');