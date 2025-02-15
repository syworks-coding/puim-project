-- User 테이블에 사용자 추가
INSERT INTO users (username, password)
VALUES ('puim', '$2a$10$jHDY3ArQrZA9FoRQDOdXJeMdYSPYBC32cPQMwp6QFadUjrnVA3zrC'); -- 12
INSERT INTO users (username, password)
VALUES ('gildong', '$2a$10$I7fSXsO76rsw5czPrDlnWu2H1Xp3aPrND4lCBj18JJ6Bo.tijVz5.'); -- password1
INSERT INTO users (username, password)
VALUES ('cheolsu', '$2a$10$mj99zjrPhawK6dLJFb/GGe1m0DIHfVj4w0Hg/dxkwmNUtVmqUYypy'); -- password2
INSERT INTO users (username, password)
VALUES ('yeonghee', '$2a$10$J/v/YQOphoPptEtADVXdFurV6wF6GElgeRX25xcqacO6HnZm2JXtS'); -- password3

-- Post 테이블에 20개의 게시글 추가
-- 홍길동의 게시글
INSERT INTO post (title, content, user_id, created_at) VALUES
('홍길동의 첫 번째 게시글', '이것은 홍길동의 첫 번째 게시글입니다.', 1, NOW()),
('홍길동의 두 번째 게시글', '이것은 홍길동의 두 번째 게시글입니다.', 1, NOW()),
('홍길동의 세 번째 게시글', '이것은 홍길동의 세 번째 게시글입니다.', 1, NOW()),
('홍길동의 네 번째 게시글', '이것은 홍길동의 네 번째 게시글입니다.', 1, NOW()),
('홍길동의 다섯 번째 게시글', '이것은 홍길동의 다섯 번째 게시글입니다.', 1, NOW()),
('홍길동의 여섯 번째 게시글', '이것은 홍길동의 여섯 번째 게시글입니다.', 1, NOW()),
('홍길동의 일곱 번째 게시글', '이것은 홍길동의 일곱 번째 게시글입니다.', 1, NOW());

-- 김철수의 게시글
INSERT INTO post (title, content, user_id, created_at) VALUES
('김철수의 첫 번째 게시글', '이것은 김철수의 첫 번째 게시글입니다.', 2, NOW()),
('김철수의 두 번째 게시글', '이것은 김철수의 두 번째 게시글입니다.', 2, NOW()),
('김철수의 세 번째 게시글', '이것은 김철수의 세 번째 게시글입니다.', 2, NOW()),
('김철수의 네 번째 게시글', '이것은 김철수의 네 번째 게시글입니다.', 2, NOW()),
('김철수의 다섯 번째 게시글', '이것은 김철수의 다섯 번째 게시글입니다.', 2, NOW()),
('김철수의 여섯 번째 게시글', '이것은 김철수의 여섯 번째 게시글입니다.', 2, NOW()),
('김철수의 일곱 번째 게시글', '이것은 김철수의 일곱 번째 게시글입니다.', 2, NOW());

-- 이영희의 게시글
INSERT INTO post (title, content, user_id, created_at) VALUES
('이영희의 첫 번째 게시글', '이것은 이영희의 첫 번째 게시글입니다.', 3, NOW()),
('이영희의 두 번째 게시글', '이것은 이영희의 두 번째 게시글입니다.', 3, NOW()),
('이영희의 세 번째 게시글', '이것은 이영희의 세 번째 게시글입니다.', 3, NOW()),
('이영희의 네 번째 게시글', '이것은 이영희의 네 번째 게시글입니다.', 3, NOW()),
('이영희의 다섯 번째 게시글', '이것은 이영희의 다섯 번째 게시글입니다.', 3, NOW()),
('이영희의 여섯 번째 게시글', '이것은 이영희의 여섯 번째 게시글입니다.', 3, NOW());

INSERT INTO comment (content, post_id, parent_id, user_id, created_at, is_deleted) VALUES
('첫 번째 댓글', 1, NULL, 1, NOW(), false),
('두 번째 댓글', 2, NULL, 2, NOW(), false),
('세 번째 댓글', 3, NULL, 3, NOW(), false),
('네 번째 댓글', 4, NULL, 1, NOW(), false),
('다섯 번째 댓글', 5, NULL, 2, NOW(), false),
('첫 번째 댓글의 대댓글', 1, 1, 3, NOW(), false),
('첫 번째 댓글의 대댓글2', 1, 1, 3, NOW(), false),
('첫 번째 댓글의 대댓글3', 1, 1, 3, NOW(), false),
('첫 번째 댓글의 대댓글4', 1, 1, 3, NOW(), false),
('두 번째 댓글의 대댓글', 2, 2, 1, NOW(), false),
('삭제된 댓글', 3, NULL, 2, NOW(), true),
('여섯 번째 댓글', 6, NULL, 3, NOW(), false),
('일곱 번째 댓글', 7, NULL, 1, NOW(), false),
('여덟 번째 댓글', 8, NULL, 2, NOW(), false),
('아홉 번째 댓글', 9, NULL, 3, NOW(), false),
('열 번째 댓글', 10, NULL, 1, NOW(), false),
('열한 번째 댓글', 11, NULL, 2, NOW(), false),
('열두 번째 댓글', 12, NULL, 3, NOW(), false),
('열세 번째 댓글', 13, NULL, 1, NOW(), false),
('열네 번째 댓글', 14, NULL, 2, NOW(), false),
('열다섯 번째 댓글', 15, NULL, 3, NOW(), false),
('열여섯 번째 댓글', 16, NULL, 1, NOW(), false),
('열일곱 번째 댓글', 17, NULL, 2, NOW(), false),
('열여덟 번째 댓글', 18, NULL, 3, NOW(), false),
('열아홉 번째 댓글', 19, NULL, 1, NOW(), false),
('스무 번째 댓글', 20, NULL, 2, NOW(), false),
('열 번째 댓글의 대댓글', 2, 2, 3, NOW(), false),
('삭제된 대댓글', 5, 5, 1, NOW(), true),
('추가된 댓글1', 1, NULL, 1, NOW(), false),
('추가된 댓글2', 1, NULL, 1, NOW(), false),
('추가된 댓글3', 1, NULL, 1, NOW(), false);


-- 홍길동이 게시글 1, 2, 3에 좋아요를 추가
INSERT INTO likes (post_id, user_id) VALUES (1, 2);
INSERT INTO likes (post_id, user_id) VALUES (2, 2);
INSERT INTO likes (post_id, user_id) VALUES (3, 2);

-- 김철수가 게시글 2, 4, 5에 좋아요를 추가
INSERT INTO likes (post_id, user_id) VALUES (2, 3);
INSERT INTO likes (post_id, user_id) VALUES (4, 3);
INSERT INTO likes (post_id, user_id) VALUES (5, 3);

-- 이영희가 게시글 3, 5, 6, 7에 좋아요를 추가
INSERT INTO likes (post_id, user_id) VALUES (3, 4);
INSERT INTO likes (post_id, user_id) VALUES (5, 4);
INSERT INTO likes (post_id, user_id) VALUES (6, 4);
INSERT INTO likes (post_id, user_id) VALUES (7, 4);

-- 홍길동이 댓글 1, 2, 3에 좋아요를 추가
INSERT INTO likes (comment_id, user_id) VALUES (1, 2);
INSERT INTO likes (comment_id, user_id) VALUES (2, 2);
INSERT INTO likes (comment_id, user_id) VALUES (3, 2);

-- 김철수가 댓글 2, 4, 5에 좋아요를 추가
INSERT INTO likes (comment_id, user_id) VALUES (2, 3);
INSERT INTO likes (comment_id, user_id) VALUES (4, 3);
INSERT INTO likes (comment_id, user_id) VALUES (5, 3);

-- 이영희가 댓글 1, 2, 3에 좋아요를 추가
INSERT INTO likes (comment_id, user_id) VALUES (1, 4);
INSERT INTO likes (comment_id, user_id) VALUES (2, 4);
INSERT INTO likes (comment_id, user_id) VALUES (3, 4);
