INSERT INTO role(role_name)
VALUES ('Admin'),
       ('Member');

INSERT INTO privilege(privilege_name)
VALUES ('READ_ACCOUNT'),
       ('WRITE_ROLE');

INSERT INTO role_privilege(role_name, privilege_name)
VALUES ('Admin', 'READ_ACCOUNT'),
       ('Admin', 'WRITE_ROLE');

INSERT INTO account(id, email, password, status, role_name)
VALUES ('00000000-0000-0000-0000-000000000001', 'admin@admin.com',
        '123456789', 'ACTIVE', 'Admin'),
       ('00000000-0000-0000-0000-000000000002', 'member@member.com',
        '123456789', 'ACTIVE', 'Member'),
       ('00000000-0000-0000-0000-000000000003', 'admin1@example.com',
        '123456789', 'ACTIVE', 'Admin'),
       ('00000000-0000-0000-0000-000000000004', 'member1@example.com',
        '123456789', 'ACTIVE', 'Member'),
       ('00000000-0000-0000-0000-000000000005', 'admin2@example.com',
        '123456789', 'ACTIVE', 'Admin'),
       ('00000000-0000-0000-0000-000000000006', 'member2@example.com',
        '123456789', 'ACTIVE', 'Member'),
       ('00000000-0000-0000-0000-000000000007', 'admin3@example.com',
        '123456789', 'ACTIVE', 'Admin'),
       ('00000000-0000-0000-0000-000000000008', 'member3@example.com',
        '123456789', 'ACTIVE', 'Member'),
       ('00000000-0000-0000-0000-000000000009', 'admin4@example.com',
        '123456789', 'ACTIVE', 'Admin'),
       ('00000000-0000-0000-0000-000000000010', 'member4@example.com',
        '123456789', 'ACTIVE', 'Member'),
       ('00000000-0000-0000-0000-000000000011', 'admin5@example.com',
        '123456789', 'ACTIVE', 'Admin'),
       ('00000000-0000-0000-0000-000000000012', 'member5@example.com',
        '123456789', 'ACTIVE', 'Member');