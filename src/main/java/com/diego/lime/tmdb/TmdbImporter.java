package com.diego.lime.tmdb;


import com.diego.lime.model.Film;
import com.diego.lime.repository.FilmRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;


@Component
@ConditionalOnProperty(name = "tmdb.import.enabled", havingValue = "true")
public class TmdbImporter implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(TmdbImporter.class);
    private static final int PAGES = 5;

    private final TmdbClient tmdbClient;
    private final FilmRepository filmRepository;

    public TmdbImporter(TmdbClient tmdbClient, FilmRepository filmRepository){
        this.tmdbClient = tmdbClient;
        this.filmRepository = filmRepository;
    }

    @Override
    public void run(String...args){
        try {
            int saved = 0;
            for (int page = 1; page <= PAGES; page ++){
                for (TmdbMovie movie : tmdbClient.getPopularMovies(page)) {
                    if (filmRepository.existsByTmdbId(movie.id())){
                        continue;
                    }
                    filmRepository.save(toFilm(movie));
                    saved++;
                }
            }
            log.info("Imported {} new films from TMDB", saved);
        } catch (RestClientException e){
            log.warn("TMDB import failed, starting with existing films: {}", e.getMessage());
        }
    }

    private Film toFilm (TmdbMovie movie){
        Integer releaseYear = parseYear(movie.releaseDate());
        return new Film(movie.title(), releaseYear, null, null, movie.id(), movie.overview(), movie.posterPath());
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
