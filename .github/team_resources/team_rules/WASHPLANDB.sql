-- CREATE DATABASE washplandb;
-- GRANT ALL PRIVILEGES ON washplandb.* TO 'ohgiraffers'@'%';
-- USE washplandb; 

DROP TABLE IF EXISTS `TBL_USER`;

CREATE TABLE IF NOT EXISTS `TBL_USER`
(
    `USER_NO`    INT NOT NULL AUTO_INCREMENT COMMENT '사용자 번호',
    `USER_ID`    VARCHAR(30) NOT NULL UNIQUE COMMENT '사용자 아이디',
    `USER_PWD`    VARCHAR(90) NOT NULL COMMENT '사용자 비밀번호',
    `EMAIL`    VARCHAR(90) NOT NULL COMMENT '사용자 이메일',
    `USER_STATUS`    VARCHAR(30) NOT NULL COMMENT '사용자 상태',
    `CANCLE_COUNT`    INT NOT NULL DEFAULT '0' COMMENT '자동취소 횟수',
    `CREATED_TIME`    DATE NOT NULL COMMENT '가입날짜',
 PRIMARY KEY ( `USER_NO` )
)ENGINE=INNODB COMMENT = '회원';

DROP TABLE IF EXISTS `TBL_MACHINE`;

CREATE TABLE IF NOT EXISTS `TBL_MACHINE`
(
    `MACHINE_NO`    INT NOT NULL COMMENT '기기 번호',
    `MACHINE_TYPE`    VARCHAR(30) NOT NULL COMMENT '기기 유형',
    `MACHINE_STATUS`    VARCHAR(30) NOT NULL DEFAULT '사용가능' COMMENT '기기 상태',
 PRIMARY KEY ( `MACHINE_NO` )
)ENGINE=INNODB COMMENT = '기기';

DROP TABLE IF EXISTS `TBL_RESERVATION`;

CREATE TABLE IF NOT EXISTS `TBL_RESERVATION`
(
    `RESERVE_NO`    INT NOT NULL AUTO_INCREMENT COMMENT '예약번호',
    `USER_NO`    INT NOT NULL COMMENT '사용자 번호',
    `MACHINE_NO`    INT NOT NULL COMMENT '기기 번호',
    `RESERVE_DATE`    DATE NOT NULL COMMENT '예약날짜',
    `START_TIME`    TIME NOT NULL COMMENT '예약시작시간',
    `END_TIME`    TIME NOT NULL COMMENT '예약종료시간',
    `RESERVE_OPTION`    VARCHAR(30) NOT NULL COMMENT '예약옵션',
    `RESERVE_PRICE`    INT NOT NULL COMMENT '예약가격',
 FOREIGN KEY(USER_NO) REFERENCES TBL_USER(USER_NO),
 FOREIGN KEY(MACHINE_NO) REFERENCES TBL_MACHINE(MACHINE_NO),
 PRIMARY KEY ( `RESERVE_NO` )
)ENGINE=INNODB COMMENT = '예약';

DROP TABLE IF EXISTS `TBL_ADMIN`;

CREATE TABLE IF NOT EXISTS `TBL_ADMIN`
(
    `ADMIN_NO`    INT NOT NULL COMMENT '관리자 번호',
    `ADMIN_ID`    VARCHAR(30) NOT NULL COMMENT '관리자 아이디',
    `ADMIN_PWD`    VARCHAR(90) NOT NULL COMMENT '관리자 비밀번호',
 PRIMARY KEY ( `ADMIN_NO` )
)ENGINE=INNODB COMMENT = '관리자';

DROP TABLE IF EXISTS `TBL_PENALTY`;

CREATE TABLE IF NOT EXISTS `TBL_PENALTY`
(
    `PENALTY_NO`    INT NOT NULL AUTO_INCREMENT COMMENT '패널티 번호',
    `USER_NO`    INT NOT NULL COMMENT '사용자 번호',
    `PENALTY_START`    DATE NOT NULL COMMENT '패널티 시작일',
    `PENALTY_END`    DATE COMMENT '패널티 종료일',
    `ADMIN_NO`    INT NOT NULL COMMENT '관리자 번호',
 FOREIGN KEY(USER_NO) REFERENCES TBL_USER(USER_NO),
 FOREIGN KEY(ADMIN_NO) REFERENCES TBL_ADMIN(ADMIN_NO),
 PRIMARY KEY ( `PENALTY_NO` )
)ENGINE=INNODB COMMENT = '패널티';

DROP TABLE IF EXISTS `TBL_FAQ`;

CREATE TABLE IF NOT EXISTS `TBL_FAQ`
(
    `FAQ_NO` INT NOT NULL COMMENT 'FAQ 번호',
    `FAQ_QUESTION` VARCHAR(100) NOT NULL COMMENT 'FAQ 질문내용',
    `FAQ_ANSWER` VARCHAR(255) NOT NULL COMMENT 'FAQ 답변내용',
 PRIMARY KEY ( `FAQ_NO` )
)ENGINE=INNODB COMMENT = 'FAQ';

DROP TABLE IF EXISTS `TBL_INQUIRY`;

CREATE TABLE IF NOT EXISTS `TBL_INQUIRY`
(
    `INQUIRY_NO`    INT NOT NULL AUTO_INCREMENT COMMENT '문의 번호',
    `USER_NO`    INT NOT NULL COMMENT '사용자 번호',
    `INQUIRY_TITLE`    VARCHAR(100) NOT NULL COMMENT '문의 제목',
    `INQUIRY_CONTENT`   VARCHAR(255) NOT NULL COMMENT '문의 내용',
    `INQUIRY_DATE`    DATE NOT NULL COMMENT '문의 날짜',
    `REPLY_STATUS`    VARCHAR(30) NOT NULL COMMENT '답변 상태',
 FOREIGN KEY(USER_NO) REFERENCES TBL_USER(USER_NO),
 PRIMARY KEY ( `INQUIRY_NO` )
)ENGINE=INNODB COMMENT = '문의사항';

DROP TABLE IF EXISTS `TBL_INQUIRY_REPLY`;

CREATE TABLE IF NOT EXISTS `TBL_INQUIRY_REPLY`
(
    `REPLY_NO`    INT NOT NULL AUTO_INCREMENT COMMENT '답변 번호',
    `ADMIN_NO`    INT NOT NULL COMMENT '관리자 번호',
    `REPLY_COMMENT`    VARCHAR(255) NOT NULL COMMENT '답변 내용',
    `INQUIRY_NO`    INT NOT NULL COMMENT '문의 번호',
 FOREIGN KEY(ADMIN_NO) REFERENCES TBL_ADMIN(ADMIN_NO),
 FOREIGN KEY(INQUIRY_NO) REFERENCES TBL_INQUIRY(INQUIRY_NO),
 PRIMARY KEY ( `REPLY_NO` )
) COMMENT = '문의답변';

DROP TABLE IF EXISTS `TBL_MACHINE_LOG`;

CREATE TABLE IF NOT EXISTS `TBL_MACHINE_LOG`
(
    `LOG_NO`    INT NOT NULL AUTO_INCREMENT COMMENT '로그 번호',
    `MACHINE_NO`    INT NOT NULL COMMENT '기기 번호',
    `ADMIN_NO`    INT NOT NULL COMMENT '관리자 번호',
    `STATUS_BEFORE`    VARCHAR(30) NOT NULL COMMENT '변경 전 상태',
    `STATUS_AFTER`    VARCHAR(30) NOT NULL COMMENT '변경 후 상태',
 FOREIGN KEY (MACHINE_NO) REFERENCES TBL_MACHINE(MACHINE_NO),
 FOREIGN KEY (ADMIN_NO) REFERENCES TBL_ADMIN(ADMIN_NO),
 PRIMARY KEY ( `LOG_NO` )
)ENGINE=INNODB COMMENT = '기기 로그';

DROP TABLE IF EXISTS `TBL_NOTICE`;

CREATE TABLE IF NOT EXISTS `TBL_NOTICE`
(
    `NOTICE_NO`    INT NOT NULL AUTO_INCREMENT COMMENT '공지사항 번호',
    `NOTICE_TITLE`    VARCHAR(100) NOT NULL COMMENT '공지사항 제목',
    `NOTICE_CONTENT`    VARCHAR(255) NULL COMMENT '공지사항 내용',
    `NOTICE_DATE`    DATE NOT NULL COMMENT '공지사항 작성날짜',
    `ADMIN_NO`    INT NOT NULL COMMENT '관리자 번호',
 FOREIGN KEY (ADMIN_NO) REFERENCES TBL_ADMIN(ADMIN_NO),
 PRIMARY KEY ( `NOTICE_NO` )
)ENGINE=INNODB COMMENT = '공지사항';

INSERT INTO `TBL_USER` () VALUES
								(NULL,'user1', 'pwd1','user1@example.com','활성',0,'2024-12-10'),
                                (NULL,'user2', 'pwd2','user2@example.com','활성',0,'2024-12-11'),
                                (NULL,'user3', 'pwd3','user3@example.com','활성',0,'2024-12-12'),
                                (NULL,'user4', 'pwd4','user4@example.com','활성',0,'2024-12-13'),
                                (NULL,'user5', 'pwd5','user5@example.com','일시정지',3,'2024-12-14'),
                                (NULL,'user6', 'pwd6','user6@example.com','활성',0,'2024-12-15'),
                                (NULL,'user7', 'pwd7','user7@example.com','활성',0,'2024-12-16'),
                                (NULL,'user8', 'pwd8','user8@example.com','활성',0,'2024-12-17'),
                                (NULL,'user9', 'pwd9','user9@example.com','활성',0,'2024-12-18'),
                                (NULL,'user10', 'pwd10','user10@example.com','영구탈퇴',6,'2024-12-19');
                                
INSERT INTO `TBL_MACHINE` () VALUES
                                   (101,'세탁기','사용가능'),
                                   (102,'세탁기','고장'),
                                   (103,'세탁기','사용가능'),
                                   (201,'건조기','사용가능'),
                                   (202,'건조기','사용가능'),
                                   (203,'건조기','사용가능');

INSERT INTO `TBL_RESERVATION` () VALUES
										(NULL,1,101,'2024-12-10','12:30','13:30','표준세탁',5000),
                                        (NULL,2,101,'2024-12-10','12:30','13:30','표준세탁',5000),
                                        (NULL,3,101,'2024-12-11','12:30','13:30','표준세탁',5000),
                                        (NULL,4,101,'2024-12-11','12:30','13:30','표준세탁',5000),
                                        (NULL,5,101,'2024-12-12','12:30','13:30','표준세탁',5000),
                                        (NULL,6,101,'2024-12-12','12:30','13:30','표준세탁',5000),
                                        (NULL,7,101,'2024-12-13','12:30','13:30','표준세탁',5000),
                                        (NULL,8,101,'2024-12-13','12:30','13:30','표준세탁',5000),
                                        (NULL,9,101,'2024-12-14','12:30','13:30','표준세탁',5000),
                                        (NULL,10,101,'2024-12-14','12:30','13:30','표준세탁',5000);
                                        

INSERT INTO `TBL_ADMIN` () VALUES
								(1,'whdudtjr0', 'Whdudtjr1824!'),
                                (2,'ghkdtnsqls','gh0407@');
                                
INSERT INTO `TBL_PENALTY` () VALUES
									(NULL,5,'2024-12-10','2024-12-13',1),
                                    (NULL,10,'2024-12-10',NULL,2);
                                    
INSERT INTO `TBL_FAQ` () VALUES
								(1,'예약은 어떻게 하나요?','예약 페이지에서 원하는 날짜와 시간을 선택하고,사용 가능한 세탁기나 건조기를 예약하시면 됩니다.
                                기기의 상태는 실시간으로 업데이트 됩니다.'),
                                (2,'예약 가능한 기기 목록은 어떻게 확인하나요?','예약 페이지에서 사용 가능한 세탁기와 건조기의 상태를 실시간으로 확인하실 수 있습니다.'),
                                (3,'세탁기와 건조기의 옵션은 무엇인가요?','세탁기는 표준세탁(30분)과 침구세탁(40분) 옵션이 있으며,건조기는 소량건조(30분)와 표준건조(40분)
                                옵션이 제공됩니다.'),
                                (4,'예약 시간이 10분 단위로 제한되는데. 왜 그런가요?','예약은 10분 단위로 가능합니다. 예약시작시간에서 1시간을 더한 후 예약을 잠그는 시스템으로,
                                효율적인 예약 관리를 위해 설정되었습니다.'),
                                (5,'다른 사람이 내 예약을 볼 수 있나요?','다른 사용자는 예약된 시간대와 기기의 상태는 확인할 수 있지만, 예약자 정보는 볼 수 없습니다.');
                                
INSERT INTO `TBL_INQUIRY` () VALUES
									(NULL,1,'문의하기 테스트1','문의하기 테스트1','2024-12-10','대기중'),
                                    (NULL,2,'문의하기 테스트2','문의하기 테스트2','2024-12-11','완료'),
                                    (NULL,3,'문의하기 테스트3','문의하기 테스트3','2024-12-12','대기중'),
                                    (NULL,4,'문의하기 테스트4','문의하기 테스트4','2024-12-13','완료'),
                                    (NULL,5,'문의하기 테스트5','문의하기 테스트5','2024-12-14','대기중'),
                                    (NULL,6,'문의하기 테스트6','문의하기 테스트6','2024-12-15','완료'),
                                    (NULL,7,'문의하기 테스트7','문의하기 테스트7','2024-12-16','대기중'),
                                    (NULL,8,'문의하기 테스트8','문의하기 테스트8','2024-12-17','완료'),
                                    (NULL,9,'문의하기 테스트9','문의하기 테스트9','2024-12-18','대기중'),
                                    (NULL,10,'문의하기 테스트10','문의하기 테스트10','2024-12-19','완료');
                                    

INSERT INTO `TBL_INQUIRY_REPLY` () VALUES
										(NULL,1,'답변하기 테스트1',2),
										(NULL,2,'답변하기 테스트2',4),
										(NULL,1,'답변하기 테스트3',6),
										(NULL,2,'답변하기 테스트4',8),
										(NULL,1,'답변하기 테스트5',10);
                                        
                                        
                                        
INSERT INTO `TBL_MACHINE_LOG` () VALUES
										(NULL,102,1,'사용가능','고장');
                                        
                                        
INSERT INTO `TBL_NOTICE` () VALUES
									(NULL,'공지사항 테스트1','공지사항 테스트1','2024-12-10',1),
                                    (NULL,'공지사항 테스트2','공지사항 테스트2','2024-12-11',2),
                                    (NULL,'공지사항 테스트3','공지사항 테스트3','2024-12-12',1),
                                    (NULL,'공지사항 테스트4','공지사항 테스트4','2024-12-13',2),
                                    (NULL,'공지사항 테스트5','공지사항 테스트5','2024-12-14',1),
                                    (NULL,'공지사항 테스트6','공지사항 테스트6','2024-12-15',2),
                                    (NULL,'공지사항 테스트7','공지사항 테스트7','2024-12-16',1),
                                    (NULL,'공지사항 테스트8','공지사항 테스트8','2024-12-17',2),
                                    (NULL,'공지사항 테스트9','공지사항 테스트9','2024-12-18',1),
                                    (NULL,'공지사항 테스트10','공지사항 테스트10','2024-12-19',2),
                                    (NULL,'공지사항 테스트11','공지사항 테스트11','2024-12-20',1),
                                    (NULL,'공지사항 테스트12','공지사항 테스트12','2024-12-21',2);
                                
                                    
                                    


