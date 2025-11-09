-- =========================
-- ✅ 데이터 초기화
-- =========================
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE theater;
TRUNCATE TABLE cinema;
SET FOREIGN_KEY_CHECKS = 1;

-- =========================
-- 🎬 CINEMA (30개)
-- =========================
INSERT INTO cinema (name, region, address, contact, created_at, updated_at) VALUES
                                                                                ('CGV 강남', '서울', '서울특별시 강남구 역삼동 814-6', '02-538-0645', NOW(), NOW()),
                                                                                ('CGV 여의도', '서울', '서울특별시 영등포구 국제금융로 10', '02-6137-5500', NOW(), NOW()),
                                                                                ('CGV 홍대', '서울', '서울특별시 마포구 양화로 153', '02-330-1800', NOW(), NOW()),
                                                                                ('CGV 용산아이파크몰', '서울', '서울특별시 용산구 한강대로23길 55', '1544-1122', NOW(), NOW()),
                                                                                ('롯데시네마 건대입구', '서울', '서울특별시 광진구 아차산로 272', '1544-8855', NOW(), NOW()),
                                                                                ('롯데시네마 노원', '서울', '서울특별시 노원구 상계동 713', '02-950-2400', NOW(), NOW()),
                                                                                ('메가박스 코엑스', '서울', '서울특별시 강남구 삼성동 159', '1544-0070', NOW(), NOW()),
                                                                                ('메가박스 상암월드컵경기장', '서울', '서울특별시 마포구 월드컵로 240', '02-307-8500', NOW(), NOW()),
                                                                                ('CGV 수원', '경기', '경기도 수원시 팔달구 인계동 1037', '031-234-1234', NOW(), NOW()),
                                                                                ('CGV 일산', '경기', '경기도 고양시 일산서구 중앙로 1426', '031-909-1234', NOW(), NOW()),
                                                                                ('롯데시네마 안양', '경기', '경기도 안양시 만안구 안양로 305', '031-467-1555', NOW(), NOW()),
                                                                                ('메가박스 분당', '경기', '경기도 성남시 분당구 황새울로 360번길 21', '031-709-1234', NOW(), NOW()),
                                                                                ('CGV 인천', '인천', '인천광역시 남동구 구월동 1465', '032-437-7800', NOW(), NOW()),
                                                                                ('롯데시네마 부평', '인천', '인천광역시 부평구 부평동 534-48', '032-504-7777', NOW(), NOW()),
                                                                                ('메가박스 송도', '인천', '인천광역시 연수구 송도동 23-1', '032-833-1122', NOW(), NOW()),
                                                                                ('CGV 부산서면', '부산', '부산광역시 부산진구 부전동 503-15', '051-818-1234', NOW(), NOW()),
                                                                                ('롯데시네마 부산본점', '부산', '부산광역시 부산진구 부전동 503-15', '051-810-2000', NOW(), NOW()),
                                                                                ('메가박스 해운대', '부산', '부산광역시 해운대구 우동 1408', '051-746-1234', NOW(), NOW()),
                                                                                ('CGV 대구', '대구', '대구광역시 중구 동성로2가 88', '053-427-7777', NOW(), NOW()),
                                                                                ('메가박스 대구이시아', '대구', '대구광역시 동구 팔공로49길 16', '053-980-1234', NOW(), NOW()),
                                                                                ('롯데시네마 대구율하', '대구', '대구광역시 동구 안심로 80', '053-791-1234', NOW(), NOW()),
                                                                                ('CGV 광주터미널', '광주', '광주광역시 서구 무진대로 904', '062-360-1234', NOW(), NOW()),
                                                                                ('메가박스 광주상무', '광주', '광주광역시 서구 치평동 1180', '062-385-1234', NOW(), NOW()),
                                                                                ('롯데시네마 광주수완', '광주', '광주광역시 광산구 장신로 98', '062-960-1234', NOW(), NOW()),
                                                                                ('CGV 울산삼산', '울산', '울산광역시 남구 삼산로 273', '052-999-1234', NOW(), NOW()),
                                                                                ('메가박스 울산진장', '울산', '울산광역시 북구 진장유통로 64', '052-281-5678', NOW(), NOW()),
                                                                                ('CGV 청주지웰시티', '충북', '충청북도 청주시 흥덕구 대농로 17', '043-716-1234', NOW(), NOW()),
                                                                                ('롯데시네마 천안', '충남', '충청남도 천안시 동남구 만남로 43', '041-559-1234', NOW(), NOW()),
                                                                                ('메가박스 전주혁신', '전북', '전라북도 전주시 완산구 기린대로 200', '063-277-1234', NOW(), NOW()),
                                                                                ('CGV 제주', '제주', '제주특별자치도 제주시 연동 263-15', '064-746-1234', NOW(), NOW());

-- =========================
-- 🎥 THEATER (각 영화관 5개 = 총 150개)
-- ScreenType(enum): _2D, _3D, IMAX, _4DX, DOLBY
-- =========================
INSERT INTO theater (name, cinema_id, seat_count, screen_type, is_available, created_at, updated_at) VALUES
-- 1
('1관', 1, 120, 'IMAX',  true, NOW(), NOW()),
('2관', 1, 110, '_2D',   true, NOW(), NOW()),
('3관', 1, 140, '_3D',   true, NOW(), NOW()),
('4DX관', 1, 100, '_4DX', true, NOW(), NOW()),
('VIP관', 1,  90, 'DOLBY', true, NOW(), NOW()),
-- 2
('1관', 2, 130, '_2D',   true, NOW(), NOW()),
('2관', 2, 150, 'IMAX',  true, NOW(), NOW()),
('3관', 2, 100, '_3D',   true, NOW(), NOW()),
('4DX관', 2,  95, '_4DX', true, NOW(), NOW()),
('VIP관', 2, 120, 'DOLBY', true, NOW(), NOW()),
-- 3
('1관', 3, 160, 'IMAX',  true, NOW(), NOW()),
('2관', 3, 140, '_2D',   true, NOW(), NOW()),
('3관', 3, 100, '_3D',   true, NOW(), NOW()),
('4DX관', 3, 110, '_4DX', true, NOW(), NOW()),
('VIP관', 3,  95, 'DOLBY', true, NOW(), NOW()),
-- 4
('1관', 4, 180, 'IMAX',  true, NOW(), NOW()),
('2관', 4, 150, '_3D',   true, NOW(), NOW()),
('3관', 4, 130, '_2D',   true, NOW(), NOW()),
('4DX관', 4, 120, '_4DX', true, NOW(), NOW()),
('VIP관', 4, 100, 'DOLBY', true, NOW(), NOW()),
-- 5
('1관', 5, 120, 'IMAX',  true, NOW(), NOW()),
('2관', 5, 110, '_2D',   true, NOW(), NOW()),
('3관', 5, 140, '_3D',   true, NOW(), NOW()),
('4DX관', 5, 100, '_4DX', true, NOW(), NOW()),
('VIP관', 5,  90, 'DOLBY', true, NOW(), NOW()),
-- 6
('1관', 6, 150, 'IMAX',  true, NOW(), NOW()),
('2관', 6, 130, '_2D',   true, NOW(), NOW()),
('3관', 6, 100, '_3D',   true, NOW(), NOW()),
('4DX관', 6, 110, '_4DX', true, NOW(), NOW()),
('VIP관', 6,  95, 'DOLBY', true, NOW(), NOW()),
-- 7
('1관', 7, 180, 'IMAX',  true, NOW(), NOW()),
('2관', 7, 160, '_2D',   true, NOW(), NOW()),
('3관', 7, 150, '_3D',   true, NOW(), NOW()),
('4DX관', 7, 100, '_4DX', true, NOW(), NOW()),
('VIP관', 7,  90, 'DOLBY', true, NOW(), NOW()),
-- 8
('1관', 8, 150, 'IMAX',  true, NOW(), NOW()),
('2관', 8, 130, '_2D',   true, NOW(), NOW()),
('3관', 8, 100, '_3D',   true, NOW(), NOW()),
('4DX관', 8, 110, '_4DX', true, NOW(), NOW()),
('VIP관', 8,  95, 'DOLBY', true, NOW(), NOW()),
-- 9
('1관', 9, 120, 'IMAX',  true, NOW(), NOW()),
('2관', 9, 110, '_2D',   true, NOW(), NOW()),
('3관', 9, 140, '_3D',   true, NOW(), NOW()),
('4DX관', 9, 100, '_4DX', true, NOW(), NOW()),
('VIP관', 9,  90, 'DOLBY', true, NOW(), NOW()),
-- 10
('1관', 10, 160, 'IMAX', true, NOW(), NOW()),
('2관', 10, 140, '_2D',  true, NOW(), NOW()),
('3관', 10, 100, '_3D',  true, NOW(), NOW()),
('4DX관', 10, 110, '_4DX', true, NOW(), NOW()),
('VIP관', 10,  95, 'DOLBY', true, NOW(), NOW()),
-- 11
('1관', 11, 130, 'IMAX', true, NOW(), NOW()),
('2관', 11, 120, '_2D',  true, NOW(), NOW()),
('3관', 11, 100, '_3D',  true, NOW(), NOW()),
('4DX관', 11, 110, '_4DX', true, NOW(), NOW()),
('VIP관', 11,  90, 'DOLBY', true, NOW(), NOW()),
-- 12
('1관', 12, 170, 'IMAX', true, NOW(), NOW()),
('2관', 12, 150, '_2D',  true, NOW(), NOW()),
('3관', 12, 130, '_3D',  true, NOW(), NOW()),
('4DX관', 12, 100, '_4DX', true, NOW(), NOW()),
('VIP관', 12,  85, 'DOLBY', true, NOW(), NOW()),
-- 13
('1관', 13, 120, 'IMAX', true, NOW(), NOW()),
('2관', 13, 110, '_2D',  true, NOW(), NOW()),
('3관', 13, 140, '_3D',  true, NOW(), NOW()),
('4DX관', 13, 100, '_4DX', true, NOW(), NOW()),
('VIP관', 13,  90, 'DOLBY', true, NOW(), NOW()),
-- 14
('1관', 14, 160, 'IMAX', true, NOW(), NOW()),
('2관', 14, 140, '_2D',  true, NOW(), NOW()),
('3관', 14, 100, '_3D',  true, NOW(), NOW()),
('4DX관', 14, 110, '_4DX', true, NOW(), NOW()),
('VIP관', 14,  95, 'DOLBY', true, NOW(), NOW()),
-- 15
('1관', 15, 130, 'IMAX', true, NOW(), NOW()),
('2관', 15, 120, '_2D',  true, NOW(), NOW()),
('3관', 15, 100, '_3D',  true, NOW(), NOW()),
('4DX관', 15, 110, '_4DX', true, NOW(), NOW()),
('VIP관', 15,  90, 'DOLBY', true, NOW(), NOW()),
-- 16
('1관', 16, 140, 'IMAX', true, NOW(), NOW()),
('2관', 16, 130, '_3D',  true, NOW(), NOW()),
('3관', 16, 120, '_2D',  true, NOW(), NOW()),
('4DX관', 16, 100, '_4DX', true, NOW(), NOW()),
('VIP관', 16,  85, 'DOLBY', true, NOW(), NOW()),
-- 17
('1관', 17, 150, 'IMAX', true, NOW(), NOW()),
('2관', 17, 140, '_2D',  true, NOW(), NOW()),
('3관', 17, 110, '_3D',  true, NOW(), NOW()),
('4DX관', 17, 100, '_4DX', true, NOW(), NOW()),
('VIP관', 17,  90, 'DOLBY', true, NOW(), NOW()),
-- 18
('1관', 18, 160, 'IMAX', true, NOW(), NOW()),
('2관', 18, 130, '_2D',  true, NOW(), NOW()),
('3관', 18, 120, '_3D',  true, NOW(), NOW()),
('4DX관', 18, 100, '_4DX', true, NOW(), NOW()),
('VIP관', 18,  95, 'DOLBY', true, NOW(), NOW()),
-- 19
('1관', 19, 130, 'IMAX', true, NOW(), NOW()),
('2관', 19, 120, '_2D',  true, NOW(), NOW()),
('3관', 19, 100, '_3D',  true, NOW(), NOW()),
('4DX관', 19,  95, '_4DX', true, NOW(), NOW()),
('VIP관', 19,  85, 'DOLBY', true, NOW(), NOW()),
-- 20
('1관', 20, 150, 'IMAX', true, NOW(), NOW()),
('2관', 20, 140, '_2D',  true, NOW(), NOW()),
('3관', 20, 110, '_3D',  true, NOW(), NOW()),
('4DX관', 20, 100, '_4DX', true, NOW(), NOW()),
('VIP관', 20,  90, 'DOLBY', true, NOW(), NOW()),
-- 21
('1관', 21, 140, 'IMAX', true, NOW(), NOW()),
('2관', 21, 130, '_2D',  true, NOW(), NOW()),
('3관', 21, 120, '_3D',  true, NOW(), NOW()),
('4DX관', 21, 100, '_4DX', true, NOW(), NOW()),
('VIP관', 21,  85, 'DOLBY', true, NOW(), NOW()),
-- 22
('1관', 22, 150, 'IMAX', true, NOW(), NOW()),
('2관', 22, 140, '_2D',  true, NOW(), NOW()),
('3관', 22, 110, '_3D',  true, NOW(), NOW()),
('4DX관', 22, 100, '_4DX', true, NOW(), NOW()),
('VIP관', 22,  90, 'DOLBY', true, NOW(), NOW()),
-- 23
('1관', 23, 160, 'IMAX', true, NOW(), NOW()),
('2관', 23, 150, '_2D',  true, NOW(), NOW()),
('3관', 23, 120, '_3D',  true, NOW(), NOW()),
('4DX관', 23, 100, '_4DX', true, NOW(), NOW()),
('VIP관', 23,  95, 'DOLBY', true, NOW(), NOW()),
-- 24
('1관', 24, 140, 'IMAX', true, NOW(), NOW()),
('2관', 24, 130, '_2D',  true, NOW(), NOW()),
('3관', 24, 120, '_3D',  true, NOW(), NOW()),
('4DX관', 24, 100, '_4DX', true, NOW(), NOW()),
('VIP관', 24,  85, 'DOLBY', true, NOW(), NOW()),
-- 25
('1관', 25, 150, 'IMAX', true, NOW(), NOW()),
('2관', 25, 140, '_2D',  true, NOW(), NOW()),
('3관', 25, 110, '_3D',  true, NOW(), NOW()),
('4DX관', 25, 100, '_4DX', true, NOW(), NOW()),
('VIP관', 25,  90, 'DOLBY', true, NOW(), NOW()),
-- 26
('1관', 26, 160, 'IMAX', true, NOW(), NOW()),
('2관', 26, 150, '_2D',  true, NOW(), NOW()),
('3관', 26, 120, '_3D',  true, NOW(), NOW()),
('4DX관', 26, 100, '_4DX', true, NOW(), NOW()),
('VIP관', 26,  95, 'DOLBY', true, NOW(), NOW()),
-- 27
('1관', 27, 140, 'IMAX', true, NOW(), NOW()),
('2관', 27, 130, '_2D',  true, NOW(), NOW()),
('3관', 27, 120, '_3D',  true, NOW(), NOW()),
('4DX관', 27, 100, '_4DX', true, NOW(), NOW()),
('VIP관', 27,  85, 'DOLBY', true, NOW(), NOW()),
-- 28
('1관', 28, 150, 'IMAX', true, NOW(), NOW()),
('2관', 28, 140, '_2D',  true, NOW(), NOW()),
('3관', 28, 110, '_3D',  true, NOW(), NOW()),
('4DX관', 28, 100, '_4DX', true, NOW(), NOW()),
('VIP관', 28,  90, 'DOLBY', true, NOW(), NOW()),
-- 29
('1관', 29, 160, 'IMAX', true, NOW(), NOW()),
('2관', 29, 150, '_2D',  true, NOW(), NOW()),
('3관', 29, 120, '_3D',  true, NOW(), NOW()),
('4DX관', 29, 100, '_4DX', true, NOW(), NOW()),
('VIP관', 29,  95, 'DOLBY', true, NOW(), NOW()),
-- 30
('1관', 30, 140, 'IMAX', true, NOW(), NOW()),
('2관', 30, 130, '_2D',  true, NOW(), NOW()),
('3관', 30, 110, '_3D',  true, NOW(), NOW()),
('4DX관', 30, 100, '_4DX', true, NOW(), NOW()),
('VIP관', 30,  90, 'DOLBY', true, NOW(), NOW());
