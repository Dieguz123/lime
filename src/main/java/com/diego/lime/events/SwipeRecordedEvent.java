package com.diego.lime.events;

import java.time.Instant;

public record SwipeRecordedEvent(
        Long roomId,
        Long userId,
        Long filmId,
        boolean liked,
        Instant occurredAt
) {
}