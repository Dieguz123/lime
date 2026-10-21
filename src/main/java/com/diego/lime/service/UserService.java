package com.diego.lime.service;

import com.diego.lime.exception.InvalidCredentialsException;
import com.diego.lime.model.User;
import com.diego.lime.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        String normalized = normalize(username);

        if (userRepository.existsByUsername(normalized)){
            throw new IllegalStateException("Username already taken");
        }
        String hash = passwordEncoder.encode(rawPassword);
        User user = new User(normalized, hash);
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }

    public User authenticate(String username, String rawPassword) {
        User user = userRepository.findByUsername(normalize(username))
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password!"));

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())){
            throw new InvalidCredentialsException("Invalid username or password!");
        }

        return user;
    }

    private String normalize(String username){
        return username.trim().toLowerCase(Locale.ROOT);
    }
}
