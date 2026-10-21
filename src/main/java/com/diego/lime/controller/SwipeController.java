package com.diego.lime.controller;

import com.diego.lime.config.CurrentUser;
import com.diego.lime.dto.SwipeRequest;
import com.diego.lime.dto.SwipeResponse;
import com.diego.lime.model.Film;
import com.diego.lime.service.SwipeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms/{code}")
public class SwipeController {

    private final SwipeService swipeService;

    public SwipeController(SwipeService swipeService) {this.swipeService = swipeService;}

    @PostMapping("/swipes")
    public ResponseEntity<SwipeResponse> createSwipe(@PathVariable String code, @Valid @RequestBody SwipeRequest request, @AuthenticationPrincipal Jwt jwt){
        boolean match = swipeService.swipe(code,CurrentUser.id(jwt) , request.filmId(), request.liked());
        return ResponseEntity.ok(new SwipeResponse(match));
    }

    @GetMapping("/next-film")
    public ResponseEntity<Film> getNextFilm(@PathVariable String code,@AuthenticationPrincipal Jwt jwt){
        return swipeService.getNextFilm(code, CurrentUser.id(jwt))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent()
                        .build());
    }

    @GetMapping("/matches")
    public ResponseEntity<List<Film>> getMatches(@PathVariable String code, @AuthenticationPrincipal Jwt jwt){
        return ResponseEntity.ok(swipeService.getMatches(code, CurrentUser.id(jwt)));
    }
}
