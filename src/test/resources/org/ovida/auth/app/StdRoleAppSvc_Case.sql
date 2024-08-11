INSERT INTO role(role_name)
VALUES ('Admin'),
       ('Member');

INSERT INTO privilege(privilege_name)
VALUES ('READ_ACCOUNT'),
       ('WRITE_ROLE');

INSERT INTO role_privilege(role_name, privilege_name)
VALUES ('Admin', 'READ_ACCOUNT'),
       ('Admin', 'WRITE_ROLE');