## DATABASE 생성확인
DROP DATABASE IF EXISTS kylemall;
CREATE DATABASE IF NOT EXISTS kylemall;
USE kylemall;

######## 회원 ########
DROP TABLE IF EXISTS member;
CREATE TABLE IF NOT EXISTS member (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(10) NOT NULL,
    pass VARCHAR(100), -- 소셜 로그인 계정은 비밀번호 없이 NULL 가능
    email VARCHAR(30) NOT NULL,
    mobile VARCHAR(13) NOT NULL,
    zipcode VARCHAR(5) NOT NULL,
    address1 VARCHAR(80) NOT NULL,
    address2 VARCHAR(60) NOT NULL,
    email_get TINYINT(1) NOT NULL,
    reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    nickname VARCHAR(20) NOT NULL,
    is_admin TINYINT(1) DEFAULT 0 NOT NULL,
    is_social TINYINT(1) DEFAULT 0 NOT NULL, -- 0: 일반 계정, 1: 소셜 계정
    social_type ENUM('none', 'kakao', 'google') DEFAULT 'none' NOT NULL, -- 소셜 로그인 유형
    profile_image VARCHAR(255) -- 프로필 이미지 URL (선택 사항)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
SELECT * FROM member;

-- 기본 채팅방 생성
-- INSERT INTO member VALUES ('admin', '관리자','');

######## 카테고리 ########
DROP TABLE IF EXISTS category;
CREATE TABLE IF NOT EXISTS category (
    category_no INTEGER AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

######## 상품 ########
DROP TABLE IF EXISTS product;
CREATE TABLE IF NOT EXISTS product (
    product_no INTEGER AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(50) NOT NULL,
    product_description TEXT NOT NULL,
    product_price INTEGER NOT NULL,
    stock_quantity INTEGER NOT NULL,
    image_url VARCHAR(255) DEFAULT '',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    category_no INTEGER NOT NULL,
    sale_ok TINYINT(1) NOT NULL,
    sale_price INTEGER NULL,
    CONSTRAINT category_no_fk FOREIGN KEY (category_no) REFERENCES category(category_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
SELECT * FROM product;

######## 장바구니 ########
DROP TABLE IF EXISTS shoppingcart;
CREATE TABLE IF NOT EXISTS shoppingcart (
    cart_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    quantity INTEGER NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    member_id VARCHAR(50) NOT NULL,
    product_no INTEGER NOT NULL,
    CONSTRAINT member_id_fk FOREIGN KEY (member_id) REFERENCES member(id),
    CONSTRAINT product_no_fk FOREIGN KEY (product_no) REFERENCES product(product_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

######## 주문 ########
DROP TABLE IF EXISTS orders;
CREATE TABLE IF NOT EXISTS orders (
    merchant_uid VARCHAR(50) PRIMARY KEY,
    member_id VARCHAR(50) NOT NULL,
    total_amount INTEGER NOT NULL,
    order_status VARCHAR(10) DEFAULT '주문 완료',
    product_title VARCHAR(50) NOT NULL,
    order_msg VARCHAR(300),
    order_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    order_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT orders_member_id_fk FOREIGN KEY (member_id) REFERENCES member(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
SELECT * FRom orders;

######## 주문상세 ########
DROP TABLE IF EXISTS orderdetail;
CREATE TABLE IF NOT EXISTS orderdetail (
    detail_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    detail_quantity INTEGER NOT NULL,
    merchant_uid VARCHAR(50) NOT NULL,
    product_no INTEGER NOT NULL,
    CONSTRAINT detail_merchant_uid_fk FOREIGN KEY (merchant_uid) REFERENCES orders(merchant_uid),
    CONSTRAINT detail_product_fk FOREIGN KEY (product_no) REFERENCES product(product_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
SELECT * FROM orderdetail;

######## 결제 ########
DROP TABLE IF EXISTS payment;
CREATE TABLE IF NOT EXISTS payment (
    imp_uid VARCHAR(50) PRIMARY KEY,         -- 상점 고유 결제 ID (PortOne API에서 필수)
    merchant_uid VARCHAR(50) NOT NULL,                    -- 연관된 주문 ID
    payment_method VARCHAR(50) NOT NULL,         -- 결제 수단 (카드, 계좌이체 등)
    payment_status ENUM('PENDING', 'PAID', 'CANCELLED', 'FAILED') DEFAULT 'PAID', -- 결제 상태
    amount INTEGER NOT NULL,              -- 결제 금액
    paid_at TIMESTAMP NULL,                      -- 결제 완료 시간
    cancelled_at TIMESTAMP NULL,                 -- 결제 취소 시간
    payment_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 레코드 생성 시간
	payment_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 레코드 업데이트 시간
    CONSTRAINT fk_order FOREIGN KEY (merchant_uid) REFERENCES orders(merchant_uid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
SELECT * FROM payment;



######## 배송 정보 ########
DROP TABLE IF EXISTS shipping;
CREATE TABLE IF NOT EXISTS shipping (
    shipping_id INTEGER AUTO_INCREMENT PRIMARY KEY, -- 배송 ID, 자동 증가
    merchant_uid VARCHAR(50) NOT NULL,                      -- 주문 ID (Foreign Key)
    recipient_name VARCHAR(100) NOT NULL,      -- 수령인 이름
    address VARCHAR(255) NOT NULL,              -- 배송 주소
    phone_number VARCHAR(15) NOT NULL,          -- 수령인 전화번호
    delivery_status ENUM('배송 준비 중', '배송 중', '배송 완료', '배송 취소') DEFAULT '배송 준비 중', -- 배송 상태
    tracking_number VARCHAR(50),                 -- 운송장 번호
    shipping_date DATETIME,                      -- 배송 시작일
    estimated_arrival DATETIME,                  -- 예상 도착일
    shipping_msg VARCHAR(300),
    shipping_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 생성일
    shipping_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 수정일
    FOREIGN KEY (merchant_uid) REFERENCES orders(merchant_uid) -- order_id는 orders 테이블의 외래 키
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
SELECT * FROM shipping;

######## 채팅방 ########
DROP TABLE IF EXISTS chat_message;  
DROP TABLE IF EXISTS chat_room_participant;
DROP TABLE IF EXISTS chat_room;

CREATE TABLE IF NOT EXISTS chat_room (
    room_id VARCHAR(50) PRIMARY KEY,
    room_name VARCHAR(100) NOT NULL,
    created_by VARCHAR(50) NOT NULL,
    room_admin VARCHAR(50) NOT NULL,
    room_password VARCHAR(100),  -- 비공개방 비밀번호 (NULL이면 공개방)
    max_users INT DEFAULT 100,   -- 최대 참여 인원
    current_users INT DEFAULT 0, -- 현재 참여 인원
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_room_creator FOREIGN KEY (created_by) REFERENCES member(id),
    CONSTRAINT fk_room_admin FOREIGN KEY (room_admin) REFERENCES member(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS chat_message (
    message_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_id VARCHAR(50) NOT NULL,
    sender VARCHAR(50) NOT NULL,
    nickname VARCHAR(20) NOT NULL,
    message TEXT NOT NULL,
    message_type VARCHAR(10) NOT NULL,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_chat_room FOREIGN KEY (room_id) REFERENCES chat_room(room_id),
    CONSTRAINT fk_chat_sender FOREIGN KEY (sender) REFERENCES member(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 채팅방 참여자 테이블 추가
CREATE TABLE IF NOT EXISTS chat_room_participant (
    room_id VARCHAR(50),
    member_id VARCHAR(50),
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (room_id, member_id),
    CONSTRAINT fk_participant_room FOREIGN KEY (room_id) REFERENCES chat_room(room_id),
    CONSTRAINT fk_participant_member FOREIGN KEY (member_id) REFERENCES member(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

######### faq 게시판 ########
DROP TABLE IF EXISTS faq;
CREATE TABLE IF NOT EXISTS faq(
    faq_no INTEGER AUTO_INCREMENT PRIMARY KEY,
    faq_title VARCHAR(50) NOT NULL,
    faq_content VARCHAR(10000) NOT NULL,
    faq_reg_date TIMESTAMP NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

####### 멤버 제약 조건 casecade 추가 ########

ALTER TABLE shoppingcart 
DROP FOREIGN KEY member_id_fk;

ALTER TABLE shoppingcart 
ADD CONSTRAINT member_id_fk 
FOREIGN KEY (member_id) REFERENCES member(id) 
ON DELETE CASCADE;

ALTER TABLE orders 
DROP FOREIGN KEY orders_member_id_fk;

ALTER TABLE orders 
ADD CONSTRAINT orders_member_id_fk 
FOREIGN KEY (member_id) REFERENCES member(id) 
ON DELETE CASCADE;

ALTER TABLE orderdetail 
DROP FOREIGN KEY detail_merchant_uid_fk;

ALTER TABLE orderdetail 
ADD CONSTRAINT detail_merchant_uid_fk 
FOREIGN KEY (merchant_uid) REFERENCES orders(merchant_uid) 
ON DELETE CASCADE;

ALTER TABLE payment 
DROP FOREIGN KEY fk_order;

ALTER TABLE payment 
ADD CONSTRAINT fk_order 
FOREIGN KEY (merchant_uid) REFERENCES orders(merchant_uid) 
ON DELETE CASCADE;

ALTER TABLE shipping 
DROP FOREIGN KEY shipping_ibfk_1;

ALTER TABLE shipping 
ADD CONSTRAINT shipping_ibfk_1 
FOREIGN KEY (merchant_uid) REFERENCES orders(merchant_uid) 
ON DELETE CASCADE;

-- 카테고리
INSERT INTO category VALUES(1, '아우터');
INSERT INTO category VALUES(2, '상의');
INSERT INTO category VALUES(3, '하의');

-- 상품
-- 상품 데이터 삽입 (30개)
INSERT INTO product (product_name, product_description, product_price, stock_quantity, image_url, created_at, updated_at, category_no, sale_ok, sale_price) VALUES
('빅사이즈 후드티', '따뜻하고 편안한 빅사이즈 후드티입니다.', 35000, 20, NULL, CURRENT_TIMESTAMP, NULL, 1, 1, 30000),
('라운드넥 니트', '부드러운 라운드넥 니트로 캐주얼한 스타일.', 25000, 15, NULL, CURRENT_TIMESTAMP, NULL, 2, 1, 20000),
('데님 청바지', '신축성 좋은 데님 소재 청바지.', 40000, 30, NULL, CURRENT_TIMESTAMP, NULL, 3, 0, NULL),
('롱패딩', '한겨울을 따뜻하게 보낼 롱패딩입니다.', 120000, 10, NULL, CURRENT_TIMESTAMP, NULL, 1, 1, 100000),
('체크 셔츠', '스타일리시한 체크 패턴 셔츠입니다.', 30000, 25, NULL, CURRENT_TIMESTAMP, NULL, 2, 0, NULL),
('슬림핏 슬랙스', '직장인 필수템, 슬림핏 슬슬랙스.', 45000, 18, NULL, CURRENT_TIMESTAMP, NULL, 3, 1, 40000),
('기모 맨투맨', '기모 소재로 보온성을 높인 맨투맨입니다.', 28000, 40, NULL, CURRENT_TIMESTAMP, NULL, 2, 1, 25000),
('울 코트', '세련된 디자인의 울 코트.', 85000, 8, 'kylemallproducts/ulcoat.jpg', CURRENT_TIMESTAMP, NULL, 1, 1, 80000),
('하프팬츠', '여름철 필수템 하프팬츠.', 20000, 50, NULL, CURRENT_TIMESTAMP, NULL, 3, 0, NULL),
('패딩 조끼', '가볍고 따뜻한 패딩 조끼입니다.', 60000, 12, 'kylemallproducts/padingjoggi.jpg', CURRENT_TIMESTAMP, NULL, 1, 1, 55000),
('헨리넥 티셔츠', '캐주얼한 스타일의 헨리넥 티셔츠.', 27000, 35, NULL, CURRENT_TIMESTAMP, NULL, 2, 0, NULL),
('와이드 팬츠', '편안함과 스타일을 동시에 제공하는 와이드 팬츠.', 42000, 20, NULL, CURRENT_TIMESTAMP, NULL, 3, 0, NULL),
('경량 패딩', '가볍고 따뜻한 경량 패딩.', 75000, 15, 'kylemallproducts/gyungranpading.jpg', CURRENT_TIMESTAMP, NULL, 1, 1, 70000),
('프린트 반팔 티', '개성 있는 프린트 디자인 반팔 티.', 22000, 60, NULL, CURRENT_TIMESTAMP, NULL, 2, 0, NULL),
('트레이닝 팬츠', '운동하기 좋은 트레이닝 팬츠.', 32000, 25, NULL, CURRENT_TIMESTAMP, NULL, 3, 1, 30000),
('양털 집업', '양털 소재로 보온성을 극대화한 집업.', 50000, 10, NULL, CURRENT_TIMESTAMP, NULL, 1, 1, 45000),
('카라 티셔츠', '깔끔한 디자인의 카라 티셔츠.', 26000, 30, NULL, CURRENT_TIMESTAMP, NULL, 2, 1, 23000),
('린넨 팬츠', '시원하고 편안한 린넨 팬츠.', 35000, 40, NULL, CURRENT_TIMESTAMP, NULL, 3, 0, NULL),
('후리스 자켓', '가벼운 후리스 소재 자켓입니다.', 48000, 12, 'kylemallproducts/hurisjaket.jpg', CURRENT_TIMESTAMP, NULL, 1, 1, 45000),
('스트라이프 티', '심플한 스트라이프 디자인 티셔츠.', 24000, 50, NULL, CURRENT_TIMESTAMP, NULL, 2, 0, NULL),
('카고 팬츠', '다용도 주머니가 특징인 카고 팬츠.', 37000, 18, NULL, CURRENT_TIMESTAMP, NULL, 3, 0, NULL),
('가죽 자켓', '고급스러운 가죽 소재의 자켓.', 130000, 5, NULL, CURRENT_TIMESTAMP, NULL, 1, 1, 125000),
('V넥 스웨터', '부드러운 V넥 디자인의 스웨터.', 27000, 28, NULL, CURRENT_TIMESTAMP, NULL, 2, 1, 25000),
('조거 팬츠', '활동성을 강조한 조거 팬츠.', 39000, 22, NULL, CURRENT_TIMESTAMP, NULL, 3, 1, 35000),
('트렌치 코트', '클래식한 디자인의 트렌치 코트.', 95000, 6, 'kylemallproducts/trenchcoat.jpg', CURRENT_TIMESTAMP, NULL, 1, 1, 90000),
('포켓 셔츠', '실용적인 포켓이 있는 셔츠.', 31000, 40, NULL, CURRENT_TIMESTAMP, NULL, 2, 0, NULL),
('밴딩 팬츠', '편안한 착용감을 제공하는 밴딩 팬츠.', 34000, 30, NULL, CURRENT_TIMESTAMP, NULL, 3, 0, NULL),
('다운 자켓', '보온성을 높인 다운 자켓.', 140000, 7, NULL, CURRENT_TIMESTAMP, NULL, 1, 1, 135000),
('스웨트 셔츠', '기본 디자인의 스웨트 셔츠.', 26000, 60, NULL, CURRENT_TIMESTAMP, NULL, 2, 1, 24000),
('배기 팬츠', '스타일리시한 배기 팬츠.', 38000, 24, NULL, CURRENT_TIMESTAMP, NULL, 3, 0, NULL);

SELECT * FROM product;

UPDATE product SET image_url = 'ulcoat.jpg' WHERE product_no = 8;
UPDATE product SET image_url = 'padingjoggi.jpg' WHERE product_no = 10;
UPDATE product SET image_url = 'gyungrangpading.jpg' WHERE product_no = 13;
UPDATE product SET image_url = 'hurisjaket.jpg' WHERE product_no = 19;
UPDATE product SET image_url = 'trenchcoat.jpg' WHERE product_no = 25;
UPDATE product SET image_url = 'goatjipup.jpg' WHERE product_no = 16;
UPDATE product SET image_url = 'gajukjaket.jpg' WHERE product_no = 22;
UPDATE product SET image_url = 'downparka.jpg' WHERE product_no = 28;