package com.diego.lime.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

    @NotBlank
    @Size(max = 30) String username,

    @NotBlank @Size(min = 8, max = 72) String password
){}
