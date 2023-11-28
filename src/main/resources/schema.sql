DROP TABLE IF EXISTS members;
DROP TABLE IF EXISTS refresh_tokens;

CREATE TABLE members (
    id         BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY                                                       COMMENT '회원 인덱스',
    name       VARCHAR(255)          NOT NULL                                                                   COMMENT '회원 이름',
    email      VARCHAR(255)          NOT NULL                                                                   COMMENT '회원 이메일',
    password   VARCHAR(255)          NOT NULL                                                                   COMMENT '회원 비밀번호',
    status     VARCHAR(255)          NOT NULL                                                                   COMMENT '회원 상태',
    authority  VARCHAR(255)          NOT NULL                                                                   COMMENT '회원 권한',
    created_at TIMESTAMP             NOT NULL             DEFAULT CURRENT_TIMESTAMP                             COMMENT '회원 가입 날짜',
    updated_at TIMESTAMP             NOT NULL             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '회원 수정 날짜'
);

CREATE TABLE refresh_tokens (
    id         BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY                                                       COMMENT '토큰 인덱스',
    rt_key     VARCHAR(255)          NOT NULL                                                                   COMMENT '토큰 키',
    rt_value   VARCHAR(255)          NOT NULL                                                                   COMMENT '토큰 값',
    created_at TIMESTAMP             NOT NULL             DEFAULT CURRENT_TIMESTAMP                             COMMENT '토큰 생성 날짜',
    updated_at TIMESTAMP             NOT NULL             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '토큰 수정 날짜'
);
