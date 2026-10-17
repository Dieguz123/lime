package com.diego.lime.controller;

import com.diego.lime.dto.SwipeRequest;
import com.diego.lime.dto.SwipeResponse;
import com.diego.lime.service.SwipeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms/{code}/swipes")
public class SwipeController {

    private final SwipeService swipeService;

    public SwipeController(SwipeService swipeService) {this.swipeService = swipeService;}

    @PostMapping
    public ResponseEntity<SwipeResponse> createSwipe(@PathVariable String code, @Valid @RequestBody SwipeRequest request){
        boolean match = swipeService.swipe(code, request.userId(), request.filmId(), request.liked());
        return ResponseEntity.ok(new SwipeResponse(match));
    }
}
