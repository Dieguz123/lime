package com.diego.lime.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record CreateRoomRequest(
        @NotBlank @Size(max = 50) String name,
        @NotNull Set<@NotNull Long> providerIds
) {}
