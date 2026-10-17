package com.diego.lime.dto;

import jakarta.validation.constraints.NotNull;

public record CreateRoomRequest(@NotNull Long creatorId) {
}
