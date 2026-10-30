CREATE TABLE room_providers (
                             room_id  BIGINT NOT NULL,
                             provider_id BIGINT NOT NULL,
                             CONSTRAINT pk_room_providers PRIMARY KEY (room_id, provider_id),
                             CONSTRAINT fk_room_providers_room FOREIGN KEY (room_id) REFERENCES rooms (id),
                             CONSTRAINT fk_room_providers_provider FOREIGN KEY (provider_id) REFERENCES providers (id)
);
