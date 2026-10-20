package com.diego.lime.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TmdbMovie
        (Long id,
         String title,
         String overwiev,
         @JsonProperty("poster_path") String posterPath,
         @JsonProperty("release_date") String releaseDate) {
}
