package com.diego.lime.dto;

import jakarta.annotation.Nullable;

public record RoomResponse(Long inviteCode, UserResponse creator, @Nullable UserResponse partner) {
}
