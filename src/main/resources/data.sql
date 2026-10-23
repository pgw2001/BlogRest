-- SAMPLE DATA:
INSERT INTO category (id, name)
VALUES (1, '개발일지');
INSERT INTO category (id, name)
VALUES (2, '자바/스프링');
INSERT INTO category (id, name)
VALUES (3, '리액트');
INSERT INTO category (id, name)
VALUES (4, '파이썬');
INSERT INTO category (id, name)
VALUES (5, '데이터베이스');
INSERT INTO category (id, name)
VALUES (6, '웹개발');
INSERT INTO category (id, name)
VALUES (7, '앱개발');
INSERT INTO category (id, name)
VALUES (8, '데브옵스');
INSERT INTO category (id, name)
VALUES (9, '알고리즘');
INSERT INTO category (id, name)
VALUES (10, 'CS지식');
INSERT INTO category (id, name)
VALUES (11, 'IT소식');
INSERT INTO category (id, name)
VALUES (12, '전자기기');
INSERT INTO category (id, name)
VALUES (13, '개발자일상');
INSERT INTO category (id, name)
VALUES (14, '커리어');
INSERT INTO category (id, name)
VALUES (15, '도서리뷰');
INSERT INTO category (id, name)
VALUES (16, '생산성');
INSERT INTO category (id, name)
VALUES (17, '블로그운영');
INSERT INTO category (id, name)
VALUES (18, '디자인');
INSERT INTO category (id, name)
VALUES (19, '비즈니스');
INSERT INTO category (id, name)
VALUES (20, '일상이야기');
INSERT INTO category (id, name)
VALUES (21, '맛집/카페');
INSERT INTO category (id, name)
VALUES (22, '여행');
INSERT INTO category (id, name)
VALUES (23, '자기계발');
INSERT INTO category (id, name)
VALUES (24, '재테크');
INSERT INTO category (id, name)
VALUES (25, '운동/건강');
INSERT INTO category (id, name)
VALUES (26, '취미생활');
INSERT INTO category (id, name)
VALUES (27, '영화리뷰');
INSERT INTO category (id, name)
VALUES (28, '게임');
INSERT INTO category (id, name)
VALUES (29, '질문답변');
INSERT INTO category (id, name)
VALUES (30, '공지사항');

ALTER TABLE CATEGORY ALTER COLUMN id RESTART WITH (SELECT MAX(id) + 1 FROM CATEGORY);

-- 회원 샘플 데이터
INSERT INTO member (id, email, password, nickname, role, created_at)
VALUES (11, 'admin@test.com', '$2b$10$nzU9bBvv/yq2QXdckCQ6z.Wys7gNrG8xbBuRd/B3q7Hsyw7CRc8ue', '관리자', 'ADMIN', CURRENT_TIMESTAMP);
INSERT INTO member (id, email, password, nickname, role, created_at)
VALUES (1, 'kim@test.com', '$2b$10$62ySQSvLH0aW1YG6OJoky.ZY1Y5oDuDmlSbrFsFu5x9g0D4ou.tJ6', '김코딩', 'USER', CURRENT_TIMESTAMP);
INSERT INTO member (id, email, password, nickname, role, created_at)
VALUES (2, 'lee@test.com', '$2b$10$jnYeSJR78QfnKWm45QmFE.WrGygyZofxCDXwvlKL66p5ZBQqCMn3K', '이자바', 'USER', CURRENT_TIMESTAMP);
INSERT INTO member (id, email, password, nickname, role, created_at)
VALUES (3, 'park@test.com', '$2b$10$DccC8sSIb4N.OR6gJx9l/ubXTFSzhRdxuhRe207s3uph.s9zUhfqa', '박스프링', 'USER', CURRENT_TIMESTAMP);
INSERT INTO member (id, email, password, nickname, role, created_at)
VALUES (4, 'choi@test.com', '$2b$10$UFGt.ekyA2EceU8nc.WchOnMnqZDo4PgYh85liBLqTiighjpoh9PW', '최백엔드', 'USER', CURRENT_TIMESTAMP);
INSERT INTO member (id, email, password, nickname, role, created_at)
VALUES (5, 'jung@test.com', '$2b$10$BRvMtVJdn9lcezZsOfUVG.8FgPtmlqcI1TBAfxGjgpSQbJcGwiIVu', '정디비', 'USER', CURRENT_TIMESTAMP);
INSERT INTO member (id, email, password, nickname, role, created_at)
VALUES (6, 'kang@test.com', '$2b$10$BGNAJMGJO7XWsxvkquQmRux2c4s/a1vVK1i402ELzREvbHhSh0uMC', '강리액트', 'USER', CURRENT_TIMESTAMP);
INSERT INTO member (id, email, password, nickname, role, created_at)
VALUES (7, 'yoon@test.com', '$2b$10$TIz3ZcjyKl7ehJACS2mq4.mR11iyr/lEHXM1tMQK/xXWb20N0DWei', '윤파이썬', 'USER', CURRENT_TIMESTAMP);
INSERT INTO member (id, email, password, nickname, role, created_at)
VALUES (8, 'lim@test.com', '$2b$10$VXSi1TdrffsblzDyPFGJV.AqiV/NkdJSB6icu7wlSwTgk/CbpWlmm', '임데브옵스', 'USER', CURRENT_TIMESTAMP);
INSERT INTO member (id, email, password, nickname, role, created_at)
VALUES (9, 'han@test.com', '$2b$10$Rgf5Hf3UtE8I5e/aZS/yeuIurkzfIktNL2.EqM1VrmL.X6878.3IO', '한알고', 'USER', CURRENT_TIMESTAMP);
INSERT INTO member (id, email, password, nickname, role, created_at)
VALUES (10, 'oh@test.com', '$2b$10$9tniXIt9pNc6i2Km9Sc9xe84l8Fhkf/GVHLaBPdee7dkHAJsKmnpy', '오퍼블', 'USER', CURRENT_TIMESTAMP);

-- 게시글 샘플 데이터 (is_visible = FALSE 2건 포함)
INSERT INTO post (id, member_id, category_id, title, content, is_visible, created_at)
VALUES (1, 1, 2, '스프링 부트 첫 프로젝트 만들기', 'Spring Initializr로 프로젝트를 생성하고 실행해봤습니다. 생각보다 설정이 간단했습니다.', TRUE, CURRENT_TIMESTAMP);
INSERT INTO post (id, member_id, category_id, title, content, is_visible, created_at)
VALUES (2, 4, 5, '데이터베이스 인덱스가 뭔가요', '인덱스를 걸면 조회는 빨라지고 쓰기는 느려진다는 걸 이제야 이해했습니다.', TRUE, CURRENT_TIMESTAMP);
INSERT INTO post (id, member_id, category_id, title, content, is_visible, created_at)
VALUES (3, 2, 2, 'JPA 연관관계 매핑 정리', '@ManyToOne과 @OneToMany의 차이, 그리고 관계의 주인이 누구인지 정리해봤습니다.', TRUE, CURRENT_TIMESTAMP);
INSERT INTO post (id, member_id, category_id, title, content, is_visible, created_at)
VALUES (4, 6, 3, '리액트 useEffect 의존성 배열', '의존성 배열을 빼먹었다가 무한 루프를 겪었습니다. 기록으로 남깁니다.', TRUE, CURRENT_TIMESTAMP);
INSERT INTO post (id, member_id, category_id, title, content, is_visible, created_at)
VALUES (5, 3, 2, '@Transactional 동작 원리', '프록시 기반으로 동작하기 때문에 같은 클래스 내부 호출에서는 적용되지 않습니다.', TRUE, CURRENT_TIMESTAMP);
INSERT INTO post (id, member_id, category_id, title, content, is_visible, created_at)
VALUES (6, 9, 9, '이분 탐색 구현할 때 실수하는 부분', 'while 조건과 mid 계산에서 오버플로가 날 수 있습니다.', TRUE, CURRENT_TIMESTAMP);
INSERT INTO post (id, member_id, category_id, title, content, is_visible, created_at)
VALUES (7, 5, 5, 'N+1 문제를 처음 만났습니다', '목록 조회 한 번에 쿼리가 스무 번 나가는 걸 로그로 확인했습니다.', TRUE, CURRENT_TIMESTAMP);
INSERT INTO post (id, member_id, category_id, title, content, is_visible, created_at)
VALUES (8, 7, 4, '파이썬 리스트 컴프리헨션', '간결하지만 너무 중첩하면 읽기 어려워집니다. 적당히 쓰는 게 좋습니다.', TRUE, CURRENT_TIMESTAMP);
INSERT INTO post (id, member_id, category_id, title, content, is_visible, created_at)
VALUES (9, 1, 10, 'HTTP 상태 코드 정리', '200, 201, 204, 400, 404, 409를 언제 쓰는지 표로 만들어봤습니다.', TRUE, CURRENT_TIMESTAMP);
INSERT INTO post (id, member_id, category_id, title, content, is_visible, created_at)
VALUES (10, 8, 8, '도커 컴포즈로 로컬 환경 구성', 'DB와 애플리케이션을 한 번에 띄우니 협업이 훨씬 편해졌습니다.', TRUE, CURRENT_TIMESTAMP);
INSERT INTO post (id, member_id, category_id, title, content, is_visible, created_at)
VALUES (11, 10, 6, 'CSS Flexbox 정렬 총정리', 'justify-content와 align-items를 계속 헷갈렸는데 이제 확실히 구분합니다.', TRUE, CURRENT_TIMESTAMP);
INSERT INTO post (id, member_id, category_id, title, content, is_visible, created_at)
VALUES (12, 2, 1, '오늘의 개발일지', '리팩터링만 세 시간 했습니다. 코드는 줄었는데 뿌듯하네요.', TRUE, CURRENT_TIMESTAMP);
INSERT INTO post (id, member_id, category_id, title, content, is_visible, created_at)
VALUES (13, 4, 14, '신입 백엔드 개발자 회고', '입사 6개월차에 느낀 점을 솔직하게 적어봤습니다.', TRUE, CURRENT_TIMESTAMP);
INSERT INTO post (id, member_id, category_id, title, content, is_visible, created_at)
VALUES (14, 6, 3, '리액트 상태 관리 라이브러리 비교', 'Redux, Zustand, Recoil을 실제로 써보고 비교했습니다.', TRUE, CURRENT_TIMESTAMP);
INSERT INTO post (id, member_id, category_id, title, content, is_visible, created_at)
VALUES (15, 3, 5, 'JPA 영속성 컨텍스트 이해하기', '1차 캐시 덕분에 같은 엔티티를 두 번 조회해도 쿼리는 한 번만 나갑니다.', TRUE, CURRENT_TIMESTAMP);
INSERT INTO post (id, member_id, category_id, title, content, is_visible, created_at)
VALUES (16, 7, 15, '클린 코드 읽고 나서', '이름 짓기에 시간을 더 써야겠다는 생각이 들었습니다.', TRUE, CURRENT_TIMESTAMP);
INSERT INTO post (id, member_id, category_id, title, content, is_visible, created_at)
VALUES (17, 5, 11, '자바 25 릴리스 소식 정리', '새로 추가된 기능들을 간단히 훑어봤습니다.', TRUE, CURRENT_TIMESTAMP);
INSERT INTO post (id, member_id, category_id, title, content, is_visible, created_at)
VALUES (18, 9, 9, '다이나믹 프로그래밍 입문', '피보나치부터 시작해서 점화식 세우는 법을 익혔습니다.', TRUE, CURRENT_TIMESTAMP);
INSERT INTO post (id, member_id, category_id, title, content, is_visible, created_at)
VALUES (19, 8, 16, '개발자를 위한 생산성 도구', '단축키만 잘 써도 하루에 삼십 분은 아낍니다.', TRUE, CURRENT_TIMESTAMP);
INSERT INTO post (id, member_id, category_id, title, content, is_visible, created_at)
VALUES (20, 10, 7, '안드로이드 첫 앱 배포기', '스토어 심사에서 두 번 반려당한 이야기입니다.', TRUE, CURRENT_TIMESTAMP);
INSERT INTO post (id, member_id, category_id, title, content, is_visible, created_at)
VALUES (21, 1, 1, '작성 중인 초안입니다', '아직 정리가 안 끝나서 비공개로 둡니다.', FALSE, CURRENT_TIMESTAMP);
INSERT INTO post (id, member_id, category_id, title, content, is_visible, created_at)
VALUES (22, 5, 17, '블로그 개편 계획 메모', '공개하기 전에 더 다듬어야 합니다.', FALSE, CURRENT_TIMESTAMP);

-- 댓글 샘플 데이터
INSERT INTO comment (id, post_id, member_id, content, created_at)
VALUES (1, 1, 2, '좋은 글 감사합니다.', CURRENT_TIMESTAMP);
INSERT INTO comment (id, post_id, member_id, content, created_at)
VALUES (2, 1, 3, '저도 같은 부분에서 막혔었는데 도움이 됐습니다.', CURRENT_TIMESTAMP);
INSERT INTO comment (id, post_id, member_id, content, created_at)
VALUES (3, 1, 5, '다음 글도 기대할게요.', CURRENT_TIMESTAMP);
INSERT INTO comment (id, post_id, member_id, content, created_at)
VALUES (4, 2, 1, '설정 부분을 조금 더 자세히 알고 싶습니다.', CURRENT_TIMESTAMP);
INSERT INTO comment (id, post_id, member_id, content, created_at)
VALUES (5, 2, 4, '실무에서도 이렇게 쓰나요?', CURRENT_TIMESTAMP);
INSERT INTO comment (id, post_id, member_id, content, created_at)
VALUES (6, 3, 7, '정리가 깔끔해서 이해가 잘 됐습니다.', CURRENT_TIMESTAMP);
INSERT INTO comment (id, post_id, member_id, content, created_at)
VALUES (7, 5, 2, '예제 코드가 특히 좋았습니다.', CURRENT_TIMESTAMP);
INSERT INTO comment (id, post_id, member_id, content, created_at)
VALUES (8, 5, 9, '저장해두고 다시 읽어야겠어요.', CURRENT_TIMESTAMP);
INSERT INTO comment (id, post_id, member_id, content, created_at)
VALUES (9, 7, 1, '비슷한 경험이 있어서 공감됩니다.', CURRENT_TIMESTAMP);
INSERT INTO comment (id, post_id, member_id, content, created_at)
VALUES (10, 12, 3, '질문이 있는데 따로 남기겠습니다.', CURRENT_TIMESTAMP);

-- 좋아요 샘플 데이터
INSERT INTO likes (id, post_id, member_id, created_at) VALUES (1, 1, 2, CURRENT_TIMESTAMP);
INSERT INTO likes (id, post_id, member_id, created_at) VALUES (2, 1, 3, CURRENT_TIMESTAMP);
INSERT INTO likes (id, post_id, member_id, created_at) VALUES (3, 1, 5, CURRENT_TIMESTAMP);
INSERT INTO likes (id, post_id, member_id, created_at) VALUES (4, 1, 7, CURRENT_TIMESTAMP);
INSERT INTO likes (id, post_id, member_id, created_at) VALUES (5, 2, 1, CURRENT_TIMESTAMP);
INSERT INTO likes (id, post_id, member_id, created_at) VALUES (6, 2, 4, CURRENT_TIMESTAMP);
INSERT INTO likes (id, post_id, member_id, created_at) VALUES (7, 3, 2, CURRENT_TIMESTAMP);
INSERT INTO likes (id, post_id, member_id, created_at) VALUES (8, 5, 9, CURRENT_TIMESTAMP);
INSERT INTO likes (id, post_id, member_id, created_at) VALUES (9, 5, 10, CURRENT_TIMESTAMP);
INSERT INTO likes (id, post_id, member_id, created_at) VALUES (10, 8, 1, CURRENT_TIMESTAMP);

-- 프로필 샘플 데이터
INSERT INTO profile (id, member_id, bio, profile_image, sns_link) VALUES (1, 1, '백엔드 공부 중입니다. 매일 기록합니다.', NULL, 'https://github.com/kimcoding');
INSERT INTO profile (id, member_id, bio, profile_image, sns_link) VALUES (2, 2, '자바와 스프링을 좋아합니다.', NULL, NULL);
INSERT INTO profile (id, member_id, bio, profile_image, sns_link) VALUES (3, 3, '스프링 연습 중입니다.', NULL, NULL);

ALTER TABLE PROFILE ALTER COLUMN id RESTART WITH (SELECT MAX(id) + 1 FROM PROFILE);
ALTER TABLE LIKES ALTER COLUMN id RESTART WITH (SELECT MAX(id) + 1 FROM LIKES);
ALTER TABLE MEMBER ALTER COLUMN id RESTART WITH (SELECT MAX(id) + 1 FROM MEMBER);
ALTER TABLE POST ALTER COLUMN id RESTART WITH (SELECT MAX(id) + 1 FROM POST);
ALTER TABLE COMMENT ALTER COLUMN id RESTART WITH (SELECT MAX(id) + 1 FROM COMMENT);
