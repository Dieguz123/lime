package com.diego.lime.controller;

import com.diego.lime.model.Film;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class FilmController {

    @GetMapping("/films")
    public List<Film> response (){
        List<Film> films = new ArrayList<>();

        films.add(new Film(1, "Quo vado", 2016, "Netflix", "Commedia"));
        films.add(new Film(2, "Inception", 2010, "Prime", "Fantascienza"));
        films.add(new Film(3, "Il Padrino", 1972, "Netflix", "Drammatico"));

        return films;
    }
}
