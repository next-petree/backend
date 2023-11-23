-- Member 데이터, 비밀번호는 '1234'의 암호화 값
-- data.sql을 이용하면 스프링시큐러티로 비밀번호를 암호화하는 과정이 건너뛰어지므로,
-- 암호화된 값을 적용하여 insert함.
insert into member(id, email, password, nickname, address, phone_number, latitude, longitude, role)
values (1, "abc123@abc.com", "$2a$10$.ujW7BsTZUnqmap6einCTOFxkz.TpQzQLjaml6Zp2.P/t3LDkeNO6", "test adopter1", "서울 강서구 강서로 271", "010-1234-1234", 37.549967407557105, 126.83614269088818, "ADOPTER");
insert into adopter(id) values (1);
insert into profile_img_file (id, member_id, original_file_name, file_name, file_url)
values (1, 1, "adopter_img1.jpeg", "adopter_img1.jpeg", "https://petree-bucket.s3.ap-northeast-2.amazonaws.com/profile-img/adopter_img1.jpeg");

insert into member(id, email, password, nickname, address, phone_number, latitude, longitude, role)
values (2, "bcd123@bcd.com", "$2a$10$.ujW7BsTZUnqmap6einCTOFxkz.TpQzQLjaml6Zp2.P/t3LDkeNO6", "test breeder1", "경기 수원시 영통구 월드컵로 206", "010-2345-2345", 37.28266159769498, 127.0444739641248, "BREEDER");
insert into breeder(id) values (2);
insert into profile_img_file (id, member_id, original_file_name, file_name, file_url)
values (2, 2, "breeder_img1.jpeg", "breeder_img1.jpeg", "https://petree-bucket.s3.ap-northeast-2.amazonaws.com/profile-img/breeder_img1.jpeg");

insert into member(id, email, password, nickname, address, phone_number, latitude, longitude, role)
values (3, "cdf234@cdf.com", "$2a$10$.ujW7BsTZUnqmap6einCTOFxkz.TpQzQLjaml6Zp2.P/t3LDkeNO6", "test breeder2", "경기 성남시 분당구 판교동 584", "010-5667-2345", 37.38917886736625, 127.09759928946589, "BREEDER");
insert into breeder(id, is_verified) values (3, true);
insert into profile_img_file (id, member_id, original_file_name, file_name, file_url)
values (3, 3, "breeder_img2.jpeg", "breeder_img1.jpeg", "https://petree-bucket.s3.ap-northeast-2.amazonaws.com/profile-img/breeder_img2.jpeg");

insert into member(id, email, password, nickname, address, phone_number, latitude, longitude, role)
values (4, "dfe123@dfe.com", "$2a$10$.ujW7BsTZUnqmap6einCTOFxkz.TpQzQLjaml6Zp2.P/t3LDkeNO6", "test adopter2", "서울 강서구 강서로 271", "010-5678-6543", 37.549967407557105, 126.83614269088818, "ADOPTER");
insert into adopter(id) values (4);
insert into profile_img_file (id, member_id, original_file_name, file_name, file_url)
values (4, 4, "adopter_img2.jpeg", "adopter_img2.jpeg", "https://petree-bucket.s3.ap-northeast-2.amazonaws.com/profile-img/adopter_img2.jpeg");


-- 브리더가 소유한 주력견종 --
insert into main_breed(id, breeder_id, type, size, feature)
values (1, 2, "토이푸들", "SMALL", "곱슬거리는 털을 가진 귀여운 소형견. 지능이 높고 애교가 많은 성격");
insert into main_breed(id, breeder_id, type, size, feature)
values (2, 2, "골든 리트리버", "LARGE", "윤기가 흐르는 크림빛 또는 금빛의 풍성한 털이 가장 큰 특징이며 은은하게 귀티가 흐르는 인상");
insert into main_breed(id, breeder_id, type, size, feature)
values (3, 3, "말티즈", "SMALL", "'몰티즈'란 이름은 이 개가 지중해의 몰타(이탈리아어: Malta) 섬이 원산지여서 붙여졌다는게 정설");
insert into main_breed(id, breeder_id, type, size, feature)
values (4, 3, "닥스훈트", "SMALL", "닥스훈트는 몸통이 길고 사지가 짧은 독일 원산의 개로서, 원래는 오소리 사냥의 전문개였다.");



-- 주력 견종에 대한 실제 견종 --
insert into dog(id, breeder_id, main_breed_id, birth_date, name, gender, management, status)
values (1, 2, 1, "2023-07-01", "쫑이", "MALE", "착하고 순해요", "AVAILABLE");
insert into dog_img_file(id, dog_id, original_file_name, file_name, file_url)
values (1, 1, "toy_puddle1.jpeg", "toy_puddle1.jpeg", "https://petree-bucket.s3.ap-northeast-2.amazonaws.com/dog-img/toy_puddle1.jpeg");

insert into dog(id, breeder_id, main_breed_id, birth_date, name, gender, management, status)
values (2, 2, 2, "2023-07-01", "하늘이", "FEMALE", "착하고 순하고 커요", "UNDERWAY");
insert into dog_img_file(id, dog_id, original_file_name, file_name, file_url)
values (2, 2, "retriever1.jpeg", "retriever1.jpeg", "https://petree-bucket.s3.ap-northeast-2.amazonaws.com/dog-img/retriever1.jpeg");

insert into dog(id, breeder_id, main_breed_id, birth_date, name, gender, management, status)
values (3, 3, 3, "2023-07-01", "꽁이", "FEMALE", "착하고 순하고 커요", "UNDERWAY");

insert into dog(id, breeder_id, main_breed_id, birth_date, name, gender, management, status)
values (4, 3, 3, "2023-07-01", "깡이", "MALE", "착하고 커요", "UNDERWAY");

insert into dog(id, breeder_id, main_breed_id, birth_date, name, gender, management, status)
values (5, 3, 4, "2023-07-01", "땡이", "FEMALE", "착하고 순하고 까매요", "AVAILABLE");