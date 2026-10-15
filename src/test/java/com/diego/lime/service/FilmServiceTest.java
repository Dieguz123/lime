package com.diego.lime.service;

import com.diego.lime.model.Film;
import com.diego.lime.repository.FilmRepository;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class FilmServiceTest {

    @Autowired
    private FilmService service;

    @Autowired
    private FilmRepository repository;



    @Test
    void returnFilmsGetAll(){
        repository.deleteAll();

        repository.save(new Film("Inception", 2010, "Prime", "Fantascienza"));
        repository.save(new Film("Star Trek", 2010, "Prime", "Fantascienza"));
        repository.save(new Film("Interstellar", 2010, "Prime", "Fantascienza"));


        List<Film> films = service.getAllFilms();

        assertEquals(3, films.size());
    }

    @Test
    void returnFilmByIdFounded(){

        Film salvato = repository.save(new Film("Inception", 2010, "Prime", "Fantascienza"));

        Optional<Film> risultato = service.getFilmById(salvato.getId());

        assertTrue(risultato.isPresent());

        Film film = risultato.get();

        assertEquals("Inception", film.getName());
    }

    @Test
    void returnFilmByIdNotToBeFound(){

        Optional<Film> film = service.getFilmById(9999L);

        assertTrue(film.isEmpty());
    }

}