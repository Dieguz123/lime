package com.diego.lime.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TmdbProvider(
        @JsonProperty("provider_id") Long id,
        @JsonProperty("provider_name") String name,
        @JsonProperty("provider_path") String logoPath
) {
}
