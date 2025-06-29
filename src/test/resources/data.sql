-- binary_contents
INSERT INTO binary_contents (id, created_at, file_name, size, content_type)
VALUES ('00000000-0000-0000-0000-000000000001', now(), 'profile1.png', 102400, 'image/png'),
       ('00000000-0000-0000-0000-000000000002', now(), 'file1.pdf', 204800, 'application/pdf');

-- users
INSERT INTO users (id, created_at, updated_at, username, email, password, profile_id)
VALUES ('11111111-1111-1111-1111-111111111111', now(), now(), '조현아', 'zzo@email.com',
        'password123!', '00000000-0000-0000-0000-000000000001'),
       ('22222222-2222-2222-2222-222222222222', now(), now(), '투현아', 'z2@email.com',
        'password123!', null);

-- user_statuses
INSERT INTO user_statuses (id, created_at, updated_at, user_id, last_active_at)
VALUES ('33333333-3333-3333-3333-333333333333', now(), now(),
        '11111111-1111-1111-1111-111111111111', now()),
       ('99999999-9999-9999-9999-999999999999', now(), now(),
        '22222222-2222-2222-2222-222222222222', now());

-- channels
INSERT INTO channels (id, created_at, updated_at, name, description, type)
VALUES ('44444444-4444-4444-4444-444444444444', now(), now(), '채널1', '통합 테스트 공개채널',
        'PUBLIC'),
       ('55555555-5555-5555-5555-555555555555', now(), now(), null, null,
        'PRIVATE');

-- messages
INSERT INTO messages (id, created_at, updated_at, content, channel_id, author_id)
VALUES ('66666666-6666-6666-6666-666666666666', now(), now(), '메시지1',
        '44444444-4444-4444-4444-444444444444', '11111111-1111-1111-1111-111111111111'),
       ('77777777-7777-7777-7777-777777777777', now(), now(), '메시지2',
        '55555555-5555-5555-5555-555555555555', '22222222-2222-2222-2222-222222222222');

-- message_attachments
INSERT INTO message_attachments (message_id, attachment_id)
VALUES ('66666666-6666-6666-6666-666666666666', '00000000-0000-0000-0000-000000000002');

-- read_statuses
INSERT INTO read_statuses (id, created_at, updated_at, user_id, channel_id, last_read_at)
VALUES ('88888888-8888-8888-8888-888888888888', now(), now(),
        '11111111-1111-1111-1111-111111111111', '44444444-4444-4444-4444-444444444444', now());
