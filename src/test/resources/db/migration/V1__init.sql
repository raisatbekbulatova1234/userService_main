CREATE TABLE users
(
    id       serial,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(80) NOT NULL,
    name     VARCHAR(100),

    PRIMARY KEY (id),
    CONSTRAINT login UNIQUE (username)
);

CREATE TABLE roles
(
    id   serial,
    name VARCHAR(50) NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT authority UNIQUE (name)
);

CREATE TABLE users_roles
(
    user_id int NOT NULL,
    role_id int NOT NULL,

    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_role_id FOREIGN KEY (role_id) REFERENCES roles (id)
);