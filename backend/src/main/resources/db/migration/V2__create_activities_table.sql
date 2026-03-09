CREATE TABLE activities (
    id               BIGSERIAL    PRIMARY KEY,
    title            VARCHAR(200) NOT NULL,
    description      TEXT,
    location         VARCHAR(300) NOT NULL,
    activity_date    TIMESTAMPTZ  NOT NULL,
    category         VARCHAR(20)  NOT NULL DEFAULT 'OTHER',
    max_participants INTEGER      NOT NULL CHECK (max_participants >= 2),
    status           VARCHAR(20)  NOT NULL DEFAULT 'OPEN',
    creator_id       BIGINT       NOT NULL REFERENCES users(id),
    created_at       TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_activities_status ON activities (status);
CREATE INDEX idx_activities_category ON activities (category);
CREATE INDEX idx_activities_activity_date ON activities (activity_date);
CREATE INDEX idx_activities_creator_id ON activities (creator_id);
