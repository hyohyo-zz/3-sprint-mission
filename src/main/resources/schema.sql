-- 테이블
-- User
CREATE TABLE users
(
    id         uuid PRIMARY KEY,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    username   varchar(50) UNIQUE       NOT NULL,
    email      varchar(100) UNIQUE      NOT NULL,
    password   varchar(60)              NOT NULL,
    profile_id uuid,
    role       varchar(20)              NOT NULL
);

-- BinaryContent
CREATE TABLE binary_contents
(
    id           uuid PRIMARY KEY,
    created_at   timestamp with time zone NOT NULL,
    file_name    varchar(255)             NOT NULL,
    size         bigint                   NOT NULL,
    content_type varchar(100)             NOT NULL
);

-- Channel
CREATE TABLE channels
(
    id          uuid PRIMARY KEY,
    created_at  timestamp with time zone NOT NULL,
    updated_at  timestamp with time zone,
    name        varchar(100),
    description varchar(500),
    type        varchar(10)              NOT NULL
);

-- Message
CREATE TABLE messages
(
    id         uuid PRIMARY KEY,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    content    text,
    channel_id uuid                     NOT NULL,
    author_id  uuid
);

-- Message.attachments
CREATE TABLE message_attachments
(
    message_id    uuid,
    attachment_id uuid,
    PRIMARY KEY (message_id, attachment_id)
);

-- ReadStatus
CREATE TABLE read_statuses
(
    id           uuid PRIMARY KEY,
    created_at   timestamp with time zone NOT NULL,
    updated_at   timestamp with time zone,
    user_id      uuid                     NOT NULL,
    channel_id   uuid                     NOT NULL,
    last_read_at timestamp with time zone NOT NULL,
    UNIQUE (user_id, channel_id)
);

CREATE TABLE IF NOT EXISTS jwt_token
(
    jti         uuid PRIMARY KEY,
    username    VARCHAR(255) NOT NULL,
    token_type  VARCHAR(16)  NOT NULL CHECK (token_type IN ('access', 'refresh')),
    issued_at   TIMESTAMPTZ  NOT NULL,
    expires_at  TIMESTAMPTZ  NOT NULL,
    revoked     BOOLEAN      NOT NULL DEFAULT FALSE,
    replaced_by VARCHAR(64)
);

-- 제약 조건
-- User (1) -> BinaryContent (1)
ALTER TABLE users
    ADD CONSTRAINT fk_user_binary_content
        FOREIGN KEY (profile_id)
            REFERENCES binary_contents (id)
            ON DELETE SET NULL;

-- Message (N) -> Channel (1)
ALTER TABLE messages
    ADD CONSTRAINT fk_message_channel
        FOREIGN KEY (channel_id)
            REFERENCES channels (id)
            ON DELETE CASCADE;

-- Message (N) -> Author (1)
ALTER TABLE messages
    ADD CONSTRAINT fk_message_user
        FOREIGN KEY (author_id)
            REFERENCES users (id)
            ON DELETE SET NULL;

-- MessageAttachment (1) -> BinaryContent (1)
ALTER TABLE message_attachments
    ADD CONSTRAINT fk_message_attachment_binary_content
        FOREIGN KEY (attachment_id)
            REFERENCES binary_contents (id)
            ON DELETE CASCADE;

-- ReadStatus (N) -> User (1)
ALTER TABLE read_statuses
    ADD CONSTRAINT fk_read_status_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE;

-- ReadStatus (N) -> User (1)
ALTER TABLE read_statuses
    ADD CONSTRAINT fk_read_status_channel
        FOREIGN KEY (channel_id)
            REFERENCES channels (id)
            ON DELETE CASCADE;

-- 테이블 컬럼 코멘트 추가
COMMENT ON COLUMN users.id IS '사용자 고유 ID';
COMMENT ON COLUMN users.username IS '사용자명 (로그인 ID)';
COMMENT ON COLUMN users.email IS '이메일 주소';
COMMENT ON COLUMN users.password IS 'BCrypt 암호화된 비밀번호';
COMMENT ON COLUMN users.role IS '사용자 권한 (ADMIN, USER)';

-- JWT 토큰 테이블 인덱스 생성
-- username 인덱스: 특정 사용자의 토큰을 조회할 때 성능 향상을 위해 생성
CREATE INDEX IF NOT EXISTS idx_tbl_jwt_token_username ON jwt_token(username);
-- expires_at 인덱스: 만료된 토큰을 정리하거나 유효한 토큰을 조회할 때 성능 향상을 위해 생성
CREATE INDEX IF NOT EXISTS idx_tbl_jwt_token_expires  ON jwt_token(expires_at);