package com.diego.lime.dto;

import jakarta.validation.constraints.NotNull;

public record SwipeRequest (
    @NotNull Long userId,
    @NotNull Long filmId,
    @NotNull Boolean liked
) {}
