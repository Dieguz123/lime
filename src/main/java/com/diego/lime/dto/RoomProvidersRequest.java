package com.diego.lime.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record RoomProvidersRequest(
        @NotNull Set<@NotNull Long> providerIds
) {}
