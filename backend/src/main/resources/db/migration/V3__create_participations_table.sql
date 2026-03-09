CREATE TABLE participations (
    id          BIGSERIAL    PRIMARY KEY,
    user_id     BIGINT       NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    activity_id BIGINT       NOT NULL REFERENCES activities(id) ON DELETE CASCADE,
    joined_at   TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    UNIQUE (user_id, activity_id)
);

CREATE INDEX idx_participations_activity_id ON participations (activity_id);
CREATE INDEX idx_participations_user_id ON participations (user_id);
