CREATE TYPE user_role AS ENUM ('editor', 'owner', 'admin');

CREATE TABLE IF NOT EXISTS users
(
    id  UUID PRIMARY KEY,
    name VARCHAR NOT NULL,
    role user_role NOT NULL
);
