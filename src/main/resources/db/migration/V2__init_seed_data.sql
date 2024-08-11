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
        '123456789', 'ACTIVE', 'Member');