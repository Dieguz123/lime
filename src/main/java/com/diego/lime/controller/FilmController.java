package com.diego.lime.controller;

import com.diego.lime.config.CurrentUser;
import com.diego.lime.model.Film;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import com.diego.lime.service.FilmService;

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
    public ResponseEntity<Film> getFilmById(@AuthenticationPrincipal Jwt jwt){
         return service.getFilmById(CurrentUser.id(jwt))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound()
                                        .build());
    }

}
