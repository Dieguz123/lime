package com.diego.lime.tmdb;


import com.diego.lime.model.Film;
import com.diego.lime.model.Genre;
import com.diego.lime.repository.FilmRepository;
import com.diego.lime.repository.GenreRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Component
@ConditionalOnProperty(name = "tmdb.import.enabled", havingValue = "true")
public class TmdbImporter implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(TmdbImporter.class);
    private static final int PAGES = 5;

    private final TmdbClient tmdbClient;
    private final FilmRepository filmRepository;
    private final GenreRepository genreRepository;

    public TmdbImporter(TmdbClient tmdbClient, FilmRepository filmRepository, GenreRepository genreRepository){
        this.tmdbClient = tmdbClient;
        this.filmRepository = filmRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public void run(String... args) {
        try {
            importGenres();

            int saved = 0;
            int updated = 0;
            for (int page = 1; page <= PAGES; page++) {
                for (TmdbMovie movie : tmdbClient.getPopularMovies(page)) {
                    Set<Genre> genres = findGenres(movie);
                    Optional<Film> existing = filmRepository.findByTmdbId(movie.id());

                    if (existing.isPresent()) {
                        Film film = existing.get();
                        if (film.getGenres().isEmpty() && !genres.isEmpty()) {
                            film.setGenres(genres);
                            filmRepository.save(film);
                            updated++;
                        }
                        continue;
                    }

                    Film film = toFilm(movie);
                    film.setGenres(genres);
                    filmRepository.save(film);
                    saved++;
                }
            }
            log.info("Imported {} new films and added genres to {} existing films", saved, updated);
        } catch (RestClientException e) {
            log.warn("TMDB import failed, starting with existing films: {}", e.getMessage());
        }
    }

    private void importGenres() {
        List<Genre> genres = tmdbClient.getGenres().stream()
                .map(genre -> new Genre(genre.id(), genre.name()))
                .toList();
        genreRepository.saveAll(genres);
        log.info("Imported {} genres from TMDB", genres.size());
    }

    private Set<Genre> findGenres(TmdbMovie movie) {
        if (movie.genreIds() == null) {
            return new HashSet<>();
        }
        return new HashSet<>(genreRepository.findAllById(movie.genreIds()));
    }

    private Film toFilm (TmdbMovie movie){
        Integer releaseYear = parseYear(movie.releaseDate());
        return new Film(movie.title(), releaseYear, null, movie.id(), movie.overview(), movie.posterPath());
    }

    private Integer parseYear(String releaseDate){
        if (releaseDate == null || releaseDate.isBlank()){
            return null;
        }
        try{
            return LocalDate.parse(releaseDate).getYear();
        }catch (DateTimeParseException e){
            return null;
        }
    }
}
