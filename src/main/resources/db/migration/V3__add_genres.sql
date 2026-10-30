CREATE TABLE genres (
                        id   BIGINT       PRIMARY KEY,
                        name VARCHAR(100) NOT NULL
);

CREATE TABLE film_genres (
                             film_id  BIGINT NOT NULL,
                             genre_id BIGINT NOT NULL,
                             CONSTRAINT pk_film_genres PRIMARY KEY (film_id, genre_id),
                             CONSTRAINT fk_film_genres_film FOREIGN KEY (film_id) REFERENCES films (id),
                             CONSTRAINT fk_film_genres_genre FOREIGN KEY (genre_id) REFERENCES genres (id)
);

ALTER TABLE films DROP COLUMN genre;