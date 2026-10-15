package com.diego.lime.service;

import com.diego.lime.model.User;
import com.diego.lime.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User createUser(String username){
        User user  = new User(username);
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }
}
