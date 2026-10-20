package com.diego.lime.service;

import com.diego.lime.model.User;
import com.diego.lime.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String username, String rawPassword){
        String normalized = username.trim().toLowerCase(Locale.ROOT);

        if (userRepository.existsByUsername(normalized)){
            throw new IllegalStateException("Username already used!");
        }
        String hash = passwordEncoder.encode(rawPassword);
        User user = new User(normalized, hash);
        return userRepository.save(user);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleDataIntegrity(DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Data conflict, try again");
    }

    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }
}
