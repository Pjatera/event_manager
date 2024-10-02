CREATE TABLE IF NOT EXISTS users
(
    id       BIGSERIAL PRIMARY KEY,
    login    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    age      INT          NOT NULL check ( age >= 18 ),
    role     VARCHAR(20)  NOT NULL
);



CREATE TABLE IF NOT EXISTS event_locations
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) not null,
    address     VARCHAR(255) not null,
    capacity    INT          not null check (capacity > 4),
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS events
(
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    owner_id        BIGINT       NOT NULL,
    max_places      INTEGER      NOT NULL,
    occupied_places INTEGER      NOT NULL,
    date            TIMESTAMP    NOT NULL,
    cost            INTEGER      NOT NULL CHECK ( cost > 0 ),
    duration        INTEGER      NOT NULL,
    location_id     BIGINT       NOT NULL,
    status          VARCHAR(20)  NOT NULL,
    FOREIGN KEY (location_id) REFERENCES event_locations (id)
);

TRUNCATE TABLE users, event_locations;




