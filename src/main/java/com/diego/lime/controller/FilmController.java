package com.diego.lime.controller;

import com.diego.lime.model.Film;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;

import java.util.*;

@RestController
public class FilmController {

    public List<Film> films = new ArrayList<>();

    public FilmController() {
        films.add(new Film(1, "Quo vado", 2016, "Netflix", "Commedia"));
        films.add(new Film(2, "Inception", 2010, "Prime", "Fantascienza"));
        films.add(new Film(3, "Il Padrino", 1972, "Netflix", "Drammatico"));
    }    

    @GetMapping("/films")
    public List<Film> response (){
        return films;
    }

    @GetMapping("/api/films/{id}")
    public ResponseEntity<Film> returnFilm (@PathVariable int id){
        return films.stream().filter(film -> film.id() == id)
        .findFirst()
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound()
        .build());
    }
}
