CREATE TABLE member (
id INT PRIMARY KEY AUTO_INCREMENT,
member_id VARCHAR(500) not null,
nickname VARCHAR(1000),
password VARCHAR(1000) NOT NULL,
address VARCHAR(1000),
phoneNumber VARCHAR(500),
role varchar(500) default 'adopter'
);


CREATE TABLE `role` (
`id` int NOT NULL AUTO_INCREMENT,
`member_id` int NOT NULL,
`name` varchar(50) NOT NULL,
PRIMARY KEY (`id`),
KEY `member_id` (`id`),
CONSTRAINT `role_ibfk_1` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`)
);