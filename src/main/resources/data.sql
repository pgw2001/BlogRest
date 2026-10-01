-- TRUNCATING TABLES:
TRUNCATE TABLE category;

-- SAMPLE DATA:
INSERT INTO category (id, name) VALUES (1, '개발일지');
INSERT INTO category (id, name) VALUES (2, '자바/스프링');
INSERT INTO category (id, name) VALUES (3, '리액트');
INSERT INTO category (id, name) VALUES (4, '파이썬');
INSERT INTO category (id, name) VALUES (5, '데이터베이스');
INSERT INTO category (id, name) VALUES (6, '웹개발');
INSERT INTO category (id, name) VALUES (7, '앱개발');
INSERT INTO category (id, name) VALUES (8, '데브옵스');
INSERT INTO category (id, name) VALUES (9, '알고리즘');
INSERT INTO category (id, name) VALUES (10, 'CS지식');
INSERT INTO category (id, name) VALUES (11, 'IT소식');
INSERT INTO category (id, name) VALUES (12, '전자기기');
INSERT INTO category (id, name) VALUES (13, '개발자일상');
INSERT INTO category (id, name) VALUES (14, '커리어');
INSERT INTO category (id, name) VALUES (15, '도서리뷰');
INSERT INTO category (id, name) VALUES (16, '생산성');
INSERT INTO category (id, name) VALUES (17, '블로그운영');
INSERT INTO category (id, name) VALUES (18, '디자인');
INSERT INTO category (id, name) VALUES (19, '비즈니스');
INSERT INTO category (id, name) VALUES (20, '일상이야기');
INSERT INTO category (id, name) VALUES (21, '맛집/카페');
INSERT INTO category (id, name) VALUES (22, '여행');
INSERT INTO category (id, name) VALUES (23, '자기계발');
INSERT INTO category (id, name) VALUES (24, '재테크');
INSERT INTO category (id, name) VALUES (25, '운동/건강');
INSERT INTO category (id, name) VALUES (26, '취미생활');
INSERT INTO category (id, name) VALUES (27, '영화리뷰');
INSERT INTO category (id, name) VALUES (28, '게임');
INSERT INTO category (id, name) VALUES (29, '질문답변');
INSERT INTO category (id, name) VALUES (30, '공지사항');

ALTER TABLE category ALTER COLUMN id RESTART WITH (SELECT MAX(id) + 1 FROM category);