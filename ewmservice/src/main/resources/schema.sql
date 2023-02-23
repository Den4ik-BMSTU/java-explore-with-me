CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR                                 NOT NULL,
    email VARCHAR                                 NOT NULL UNIQUE,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT name_is_blank CHECK (name NOT LIKE ' ' AND name NOT LIKE ''),
    CONSTRAINT email_is_not_correct CHECK (email LIKE '%@%')
);

CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR                                 NOT NULL UNIQUE,
    CONSTRAINT pk_categories PRIMARY KEY (id),
    CONSTRAINT name_is_blank CHECK (name NOT LIKE ' ' AND name NOT LIKE '')
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation         VARCHAR                                 NOT NULL,
    category_id        BIGINT                                  NOT NULL,
    confirmed_requests BIGINT                                  NOT NULL,
    created_on         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    description        VARCHAR                                 NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    initiator_id       BIGINT                                  NOT NULL,
    lat                FLOAT                                   NOT NULL,
    lon                FLOAT                                   NOT NULL,
    paid               BOOLEAN                                 NOT NULL,
    participant_limit  BIGINT                                  NOT NULL,
    published_on       TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    request_moderation BOOLEAN                                 NOT NULL,
    state              VARCHAR                                 NOT NULL,
    title              VARCHAR                                 NOT NULL,
    views              BIGINT                                  NOT NULL,
    state_action       VARCHAR,
    CONSTRAINT pk_events PRIMARY KEY (id),
    CONSTRAINT annotation_is_blank CHECK (annotation NOT LIKE ' ' AND annotation NOT LIKE ''),
    CONSTRAINT description_is_blank CHECK (description NOT LIKE ' ' AND description NOT LIKE ''),
    CONSTRAINT title_is_blank CHECK (title NOT LIKE ' ' AND title NOT LIKE ''),
    CONSTRAINT fk_events_to_users FOREIGN KEY (initiator_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_events_to_categories FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created      TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    event_id     BIGINT                                  NOT NULL,
    requester_id BIGINT                                  NOT NULL,
    status       VARCHAR                                 NOT NULL,
    CONSTRAINT pk_requests PRIMARY KEY (id),
    CONSTRAINT fk_requests_to_events FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE,
    CONSTRAINT fk_requests_to_users FOREIGN KEY (requester_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned BOOLEAN                                 NOT NULL,
    title  VARCHAR                                 NOT NULL,
    CONSTRAINT pk_compilations PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events_compilations
(
    id             BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    compilation_id BIGINT                                  NOT NULL,
    event_id       BIGINT,
    CONSTRAINT pk_events_compilations PRIMARY KEY (id),
    CONSTRAINT fk_events_compilations_to_compilations FOREIGN KEY (compilation_id) REFERENCES compilations (id) ON DELETE CASCADE,
    CONSTRAINT fk_events_compilations_to_events FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE
);
