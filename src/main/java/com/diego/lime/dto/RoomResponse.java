package com.diego.lime.dto;

import com.diego.lime.model.Room;
import jakarta.annotation.Nullable;

public record RoomResponse(String inviteCode, UserResponse creator, @Nullable UserResponse partner) {

    public static RoomResponse from(Room room) {
        return new RoomResponse(
                room.getInviteCode(),
                UserResponse.from(room.getCreator()),
                room.getPartner() != null ? UserResponse.from(room.getPartner()) : null
        );
    }
}
