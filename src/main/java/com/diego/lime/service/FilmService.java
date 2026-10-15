package com.diego.lime.service;

import java.util.Objects;
import java.util.Optional;
import java.util.List;
import com.diego.lime.model.Film;
import java.util.ArrayList;
import org.springframework.stereotype.Service;


@Service
public class FilmService{

    private final List<Film> films = new ArrayList<>();

    public  FilmService() {
        films.add(new Film( "Quo vado", 2016, "Netflix", "Commedia"));
        films.add(new Film( "Inception", 2010, "Prime", "Fantascienza"));
        films.add(new Film( "Il Padrino", 1972, "Netflix", "Drammatico"));
    }  

    public List<Film> getAllFilms(){
        return films; 
    }

    public Optional<Film> getFilmById (Long id){
        return films.stream().filter(film -> Objects.equals(film.getId(), id))
                .findFirst();
    }



} 