-- User 테이블에 3명의 사용자 추가
INSERT INTO users (user_id, user_pw) VALUES ('puim', '12');
INSERT INTO users (user_id, user_pw) VALUES ('홍길동', 'password1');
INSERT INTO users (user_id, user_pw) VALUES ('김철수', 'password2');
INSERT INTO users (user_id, user_pw) VALUES ('이영희', 'password3');

-- Post 테이블에 20개의 게시글 추가
-- 홍길동
INSERT INTO post (title, content, user_id) VALUES ('홍길동의 첫 번째 게시글', '이것은 홍길동의 첫 번째 게시글입니다.', 1);
INSERT INTO post (title, content, user_id) VALUES ('홍길동의 두 번째 게시글', '이것은 홍길동의 두 번째 게시글입니다.', 1);
INSERT INTO post (title, content, user_id) VALUES ('홍길동의 세 번째 게시글', '이것은 홍길동의 세 번째 게시글입니다.', 1);
-- 김철수
INSERT INTO post (title, content, user_id) VALUES ('김철수의 첫 번째 게시글', '이것은 김철수의 첫 번째 게시글입니다.', 2);
INSERT INTO post (title, content, user_id) VALUES ('김철수의 두 번째 게시글', '이것은 김철수의 두 번째 게시글입니다.', 2);
INSERT INTO post (title, content, user_id) VALUES ('김철수의 세 번째 게시글', '이것은 김철수의 세 번째 게시글입니다.', 2);
-- 이영희
INSERT INTO post (title, content, user_id) VALUES ('이영희의 첫 번째 게시글', '이것은 이영희의 첫 번째 게시글입니다.', 3);
INSERT INTO post (title, content, user_id) VALUES ('이영희의 두 번째 게시글', '이것은 이영희의 두 번째 게시글입니다.', 3);
INSERT INTO post (title, content, user_id) VALUES ('이영희의 세 번째 게시글', '이것은 이영희의 세 번째 게시글입니다.', 3);

-- 추가로 한글 게시글 11개
INSERT INTO post (title, content, user_id) VALUES ('홍길동의 네 번째 게시글', '이것은 홍길동의 네 번째 게시글입니다.', 1);
INSERT INTO post (title, content, user_id) VALUES ('홍길동의 다섯 번째 게시글', '이것은 홍길동의 다섯 번째 게시글입니다.', 1);
INSERT INTO post (title, content, user_id) VALUES ('홍길동의 여섯 번째 게시글', '이것은 홍길동의 여섯 번째 게시글입니다.', 1);
INSERT INTO post (title, content, user_id) VALUES ('김철수의 네 번째 게시글', '이것은 김철수의 네 번째 게시글입니다.', 2);
INSERT INTO post (title, content, user_id) VALUES ('김철수의 다섯 번째 게시글', '이것은 김철수의 다섯 번째 게시글입니다.', 2);
INSERT INTO post (title, content, user_id) VALUES ('김철수의 여섯 번째 게시글', '이것은 김철수의 여섯 번째 게시글입니다.', 2);
INSERT INTO post (title, content, user_id) VALUES ('이영희의 네 번째 게시글', '이것은 이영희의 네 번째 게시글입니다.', 3);
INSERT INTO post (title, content, user_id) VALUES ('이영희의 다섯 번째 게시글', '이것은 이영희의 다섯 번째 게시글입니다.', 3);
INSERT INTO post (title, content, user_id) VALUES ('이영희의 여섯 번째 게시글', '이것은 이영희의 여섯 번째 게시글입니다.', 3);
INSERT INTO post (title, content, user_id) VALUES ('홍길동의 일곱 번째 게시글', '이것은 홍길동의 일곱 번째 게시글입니다.', 1);
INSERT INTO post (title, content, user_id) VALUES ('김철수의 일곱 번째 게시글', '이것은 김철수의 일곱 번째 게시글입니다.', 2);
