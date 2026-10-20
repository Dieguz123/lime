package com.diego.lime.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TmdbMovie
        (Long id,
         String title,
         String overview,
         @JsonProperty("poster_path") String posterPath,
         @JsonProperty("release_date") String releaseDate) {
}
