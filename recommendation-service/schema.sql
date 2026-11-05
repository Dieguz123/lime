CREATE TABLE IF NOT EXISTS films (
    film_id BIGINT PRIMARY KEY,
    genre_ids BIGINT[] NOT NULL
);

CREATE TABLE IF NOT EXISTS swipes (
    room_id BIGINT NOT NULL,
    film_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    liked BOOLEAN NOT NULL,
    occurred_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT pk_swipes PRIMARY KEY (room_id, film_id, user_id)
);
