CREATE TYPE example.user_role AS ENUM ('editor', 'owner', 'admin');

CREATE TABLE IF NOT EXISTS example.users
(
    id  UUID PRIMARY KEY,
    name VARCHAR NOT NULL,
    role user_role NOT NULL
);
