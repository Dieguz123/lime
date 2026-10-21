package com.diego.lime.controller;

import com.diego.lime.dto.LoginRequest;
import com.diego.lime.dto.LoginResponse;
import com.diego.lime.dto.RegisterRequest;
import com.diego.lime.dto.UserResponse;
import com.diego.lime.model.User;
import com.diego.lime.service.TokenService;
import com.diego.lime.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final UserService service;
    private final TokenService tokenService;

    public AuthController(UserService service, TokenService tokenService){
        this.service = service;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request){
        User created = service.register(request.username(), request.password());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(created));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request){
        User authenticated = service.authenticate(request.username(), request.password());
        String token = tokenService.createToken(authenticated);
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(Objects.requireNonNull(jwt.getSubject(), "Token without subject"));
        return service.getUserById(userId)
                .map(UserResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
