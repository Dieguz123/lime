package com.diego.lime.controller;

import com.diego.lime.dto.RegisterRequest;
import com.diego.lime.dto.UserResponse;
import com.diego.lime.model.User;
import com.diego.lime.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final UserService service;

    public AuthController(UserService service){
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request){
        User created = service.register(request.username(), request.password() );
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(created));
    }
}
