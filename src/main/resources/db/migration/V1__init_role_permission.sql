CREATE TABLE role
(
  role_name  TEXT PRIMARY KEY,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  version    INT       NOT NULL DEFAULT 0
);

CREATE INDEX idx_role_created_at ON role (created_at);
CREATE INDEX idx_role_updated_at ON role (updated_at);

CREATE TABLE account
(
  id         UUID PRIMARY KEY,
  email      TEXT UNIQUE NOT NULL,
  password   TEXT        NOT NULL,
  status     TEXT        NOT NULL,
  role_name  TEXT        NOT NULL,
  created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  version    INT         NOT NULL DEFAULT 0,

  FOREIGN KEY (role_name) REFERENCES role (role_name)
);

CREATE INDEX idx_account_status ON account (status);
CREATE INDEX idx_account_created_at ON account (created_at);
CREATE INDEX idx_account_updated_at ON account (updated_at);

CREATE TABLE privilege
(
  privilege_name TEXT PRIMARY KEY,
  created_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  version        INT       NOT NULL DEFAULT 0
);

CREATE INDEX idx_privilege_created_at ON privilege (created_at);
CREATE INDEX idx_privilege_updated_at ON privilege (updated_at);

CREATE TABLE role_privilege
(
  role_name      TEXT      NOT NULL,
  privilege_name TEXT      NOT NULL,
  created_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  PRIMARY KEY (role_name, privilege_name),
  FOREIGN KEY (role_name) REFERENCES role (role_name) ON DELETE CASCADE,
  FOREIGN KEY (privilege_name) REFERENCES privilege (privilege_name) ON DELETE CASCADE
);