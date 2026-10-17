package com.diego.lime.dto;

import jakarta.validation.constraints.NotNull;

public record JoinRoomRequest(@NotNull Long userId) {
}
