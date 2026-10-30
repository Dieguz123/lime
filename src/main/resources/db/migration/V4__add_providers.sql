CREATE TABLE providers (
                        id          BIGINT       PRIMARY KEY,
                        name        VARCHAR(100) NOT NULL,
                        logo_path   VARCHAR(255)
);

CREATE TABLE film_providers (
                             film_id  BIGINT NOT NULL,
                             provider_id BIGINT NOT NULL,
                             CONSTRAINT pk_film_providers PRIMARY KEY (film_id, provider_id),
                             CONSTRAINT fk_film_providers_film FOREIGN KEY (film_id) REFERENCES films (id),
                             CONSTRAINT fk_film_providers_provider FOREIGN KEY (provider_id) REFERENCES providers (id)
);

ALTER TABLE films DROP COLUMN platform;