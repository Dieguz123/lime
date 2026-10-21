package com.diego.lime.dto;

import com.diego.lime.model.User;

//Used for avoiding response with the user's password in it
public record UserResponse(Long id, String username) {

    public static UserResponse from(User user){
        return new UserResponse(user.getId(), user.getUsername());
    }
}
