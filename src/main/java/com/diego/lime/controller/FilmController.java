package com.diego.lime.controller;

import com.diego.lime.model.Film;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import com.diego.lime.service.FilmService;

import java.util.*;

@RestController
public class FilmController {

    private final FilmService service;

    public FilmController(FilmService service){
        this.service = service;
    }

    @GetMapping("/films")
    public List<Film> response() {
        return service.returnFilms();
    }

    @GetMapping("/api/films/{id}")
    public ResponseEntity<Film> returnFilm(@PathVariable int id){
         return service.returnFilmById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound()
                                        .build());
    }

}
