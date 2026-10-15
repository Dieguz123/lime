package com.diego.lime.service;

import java.util.List;
import java.util.Optional;

import com.diego.lime.model.Film;

import com.diego.lime.repository.FilmRepository;
import org.springframework.stereotype.Service;


@Service
public class FilmService {

    private final FilmRepository filmRepository;

    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public List<Film> getAllFilms() {
        return filmRepository.findAll();
    }

    public Optional<Film> getFilmById(Long id) {
        return filmRepository.findById(id);

    }

} 