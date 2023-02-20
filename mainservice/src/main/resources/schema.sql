CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(320)                            NOT NULL,
    email VARCHAR(100)                            NOT NULL UNIQUE,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT name_is_blank CHECK (name NOT LIKE ' ' AND name NOT LIKE ''),
    CONSTRAINT email_is_not_correct CHECK (email LIKE '%@%')
    );

CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(320)                            NOT NULL UNIQUE,
    CONSTRAINT pk_categories PRIMARY KEY (id),
    CONSTRAINT name_is_blank CHECK (name NOT LIKE ' ' AND name NOT LIKE '')
    );

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation         VARCHAR(320)                            NOT NULL,
    category_id        BIGINT                                  NOT NULL,
    confirmed_requests BIGINT                                  NOT NULL,
    created_on         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    description        VARCHAR(320)                            NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    initiator_id       BIGINT                                  NOT NULL,
    lat                FLOAT                                   NOT NULL,
    lon                FLOAT                                   NOT NULL,
    paid               BOOLEAN                                 NOT NULL,
    participant_limit  BIGINT                                  NOT NULL,
    published_on       TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    request_moderation BOOLEAN                                 NOT NULL,
    state              VARCHAR(320)                            NOT NULL,
    title              VARCHAR(320)                            NOT NULL,
    views              BIGINT                                  NOT NULL,
    state_action       VARCHAR(320),
    CONSTRAINT pk_events PRIMARY KEY (id),
    CONSTRAINT fk_events_to_users FOREIGN KEY (initiator_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_events_to_categories FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE
    );