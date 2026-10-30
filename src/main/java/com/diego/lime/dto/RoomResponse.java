package com.diego.lime.dto;

import com.diego.lime.model.Room;
import jakarta.annotation.Nullable;

import java.util.Comparator;
import java.util.List;

public record RoomResponse(String inviteCode, UserResponse creator, @Nullable UserResponse partner, List<ProviderResponse> providers) {

    public static RoomResponse from(Room room) {
        return new RoomResponse(
                room.getInviteCode(),
                UserResponse.from(room.getCreator()),
                room.getPartner() != null ? UserResponse.from(room.getPartner()) : null,
                room.getProviders().stream()
                        .map(ProviderResponse::from)
                        .sorted(Comparator.comparing(ProviderResponse::name))
                        .toList()
        );
    }
}
