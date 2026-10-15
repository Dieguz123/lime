package com.diego.lime.service;

import java.util.Optional;
import java.util.List;
import com.diego.lime.model.Film;
import java.util.ArrayList;
import org.springframework.stereotype.Service;


@Service
public class FilmService{

    private final List<Film> films = new ArrayList<>();

    public  FilmService() {
        films.add(new Film(1, "Quo vado", 2016, "Netflix", "Commedia"));
        films.add(new Film(2, "Inception", 2010, "Prime", "Fantascienza"));
        films.add(new Film(3, "Il Padrino", 1972, "Netflix", "Drammatico"));
    }  

    public List<Film> getAllFilms(){
        return films; 
    }

    public Optional<Film> getFilmById (int id){
        return films.stream().filter(film -> film.id() == id)
                .findFirst();
    }



} 