package com.diego.lime.controller;

import com.diego.lime.model.Film;
import com.diego.lime.service.FilmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/films")
public class FilmController {

    private final FilmService service;

    public FilmController(FilmService service){
        this.service = service;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return service.getAllFilms();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable Long id){
         return service.getFilmById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound()
                                        .build());
    }

}
