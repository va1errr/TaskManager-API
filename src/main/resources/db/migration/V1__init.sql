CREATE TABLE tasks (
    id BIGSERIAL PRIMARY KEY,

    title TEXT NOT NULL CHECK (char_length(title) > 0),

    description TEXT,

    status TEXT NOT NULL DEFAULT 'TODO'
        CHECK (STATUS IN ('TODO', 'IN_PROGRESS', 'DONE')),

    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);