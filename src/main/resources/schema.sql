DROP TABLE IF EXISTS DISH CASCADE;
DROP TABLE IF EXISTS MENU_ITEM CASCADE;
DROP TABLE IF EXISTS RESTAURANT CASCADE;
DROP TABLE IF EXISTS USER_ROLES CASCADE;
DROP TABLE IF EXISTS USERS CASCADE;
DROP TABLE IF EXISTS VOTE CASCADE;

CREATE TABLE USERS
(
    id         INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name       VARCHAR(255)            NOT NULL,
    email      VARCHAR(255)            NOT NULL,
    password   VARCHAR(255)            NOT NULL,
    registered TIMESTAMP DEFAULT now() NOT NULL,
    enabled    BOOL      DEFAULT TRUE  NOT NULL,
    CONSTRAINT users_email_idx UNIQUE (email)
);

CREATE TABLE USER_ROLES
(
    user_id INTEGER NOT NULL,
    role    VARCHAR(255),
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES USERS (id) ON DELETE CASCADE
);

CREATE TABLE RESTAURANT
(
    id    INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    phone VARCHAR(255) NOT NULL
);

CREATE TABLE DISH
(
    id            INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    price         DOUBLE       NOT NULL,
    restaurant_id INTEGER      NOT NULL,
    CONSTRAINT dish_restaurant_idx UNIQUE (id, restaurant_id),
    FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE
);

CREATE TABLE MENU_ITEM
(
    id            INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    actual_date    DATE    NOT NULL,
    dish_id INTEGER NOT NULL,
    CONSTRAINT menu_idx UNIQUE (id, dish_id)
);

/*CREATE TABLE MENU_DISH
(
    menu_id INTEGER NOT NULL,
    dish_id INTEGER NOT NULL,
    CONSTRAINT menu_dish_idx UNIQUE (menu_id, dish_id),
    FOREIGN KEY (menu_id) REFERENCES menuItem (id) ON DELETE CASCADE
);*/

CREATE TABLE VOTE
(
    id            INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    actual_date   DATE DEFAULT CURRENT_DATE NOT NULL,
    user_id       INTEGER                   NOT NULL,
    restaurant_id INTEGER                   NOT NULL,
    CONSTRAINT vote_date_user_idx UNIQUE (actual_date, user_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE
);