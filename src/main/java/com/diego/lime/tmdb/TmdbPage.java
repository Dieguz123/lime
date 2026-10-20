package com.diego.lime.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TmdbPage(
        int page,
        List<TmdbMovie> results,
        @JsonProperty("total_pages") int totalPages
) {
}
