CREATE TABLE IF NOT EXISTS users (
    id                      UUID        DEFAULT uuidv7(),
    auth_subject            TEXT        NOT NULL,
    notification_enabled    BOOLEAN     NOT NULL,
    notification_email      TEXT        NOT NULL,
    notification_time       TIME        NOT NULL,
    time_zone               TEXT        NOT NULL,
    language                TEXT        NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uq_users_auth_subject UNIQUE (auth_subject),
    CONSTRAINT uq_users_notification_email UNIQUE (notification_email)
);

CREATE TABLE IF NOT EXISTS curriculums (
    id                      UUID        DEFAULT uuidv7(),
    user_id                 UUID        NOT NULL,
    queue_position          SMALLINT    NOT NULL, -- queue_position = -1 means the curriculum is finished
    topic                   TEXT        NOT NULL,
    current_unit_number     SMALLINT    NOT NULL,
    CONSTRAINT pk_curriculums PRIMARY KEY (id),
    CONSTRAINT fk_curriculums_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_curriculums_user_id_queue_position ON curriculums (user_id, queue_position);

CREATE TABLE IF NOT EXISTS learning_units (
    id                      UUID        DEFAULT uuidv7(),
    curriculum_id           UUID        NOT NULL,
    number                  SMALLINT    NOT NULL,
    heading                 TEXT        NOT NULL,
    subheading              TEXT        NOT NULL,
    content                 TEXT,
    CONSTRAINT pk_learning_units PRIMARY KEY (id),
    CONSTRAINT fk_learning_units_curriculum_id FOREIGN KEY (curriculum_id) REFERENCES curriculums(id) ON DELETE CASCADE
);
CREATE INDEX idx_learning_units_curriculum_id_number ON learning_units (curriculum_id, number);
