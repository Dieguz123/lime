package com.diego.lime.service;

import com.diego.lime.model.Film;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FilmServiceTest {


    @Test
    void returnFilmsRestituisceTuttiIFilm(){
        FilmService service = new FilmService();

        List<Film> films = service.getAllFilms();

        assertEquals(3, films.size());
    }

    @Test
    void returnFilmByIdTrovaIlFilmGiusto(){
        FilmService service = new FilmService();

        Optional<Film> risultato = service.getFilmById(2L);

        assertTrue(risultato.isPresent());

        Film film = risultato.get();

        assertTrue(risultato.isPresent());
        assertEquals("Inception", film.getName());
    }

    @Test
    void returnFilmByIdConIdInesistenteRestituisceVuoto(){
        FilmService service = new FilmService();

        Optional<Film> film = service.getFilmById(10L);

        assertTrue(film.isEmpty());
    }

}