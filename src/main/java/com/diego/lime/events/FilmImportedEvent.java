package com.diego.lime.events;

import com.diego.lime.model.Film;
import com.diego.lime.model.Genre;
import com.diego.lime.model.Provider;

import java.util.List;

public record FilmImportedEvent(
        Long filmId,
        Long tmdbId,
        String title,
        Integer releaseYear,
        List<Long> genreIds,
        List<Long> providerIds
) {
    public static FilmImportedEvent from(Film film) {
        return new FilmImportedEvent(
                film.getId(),
                film.getTmdbId(),
                film.getName(),
                film.getReleaseYear(),
                film.getGenres().stream().map(Genre::getId).toList(),
                film.getProviders().stream().map(Provider::getId).toList()
        );
    }
}