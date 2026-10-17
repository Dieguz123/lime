package com.diego.lime.controller;

import com.diego.lime.dto.SwipeRequest;
import com.diego.lime.dto.SwipeResponse;
import com.diego.lime.model.Film;
import com.diego.lime.service.FilmService;
import com.diego.lime.service.SwipeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms/{code}")
public class SwipeController {

    private final SwipeService swipeService;

    public SwipeController(SwipeService swipeService) {this.swipeService = swipeService;}

    @PostMapping("/swipes")
    public ResponseEntity<SwipeResponse> createSwipe(@PathVariable String code, @Valid @RequestBody SwipeRequest request){
        boolean match = swipeService.swipe(code, request.userId(), request.filmId(), request.liked());
        return ResponseEntity.ok(new SwipeResponse(match));
    }

    @GetMapping("/next-film")
    public ResponseEntity<Film> getNextFilm(@PathVariable String code, @RequestParam Long userId){
        return swipeService.getNextFilm(code, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent()
                        .build());
    }

    @GetMapping("/matches")
    public ResponseEntity<List<Film>> getMatches(@PathVariable String code){
        return ResponseEntity.ok(swipeService.getMatches(code));
    }
}
