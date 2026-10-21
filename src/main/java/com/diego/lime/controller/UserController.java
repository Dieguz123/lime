package com.diego.lime.controller;

import com.diego.lime.dto.UserResponse;
import com.diego.lime.model.User;
import com.diego.lime.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service){
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){
        return service.getUserById(id)
                .map(UserResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
