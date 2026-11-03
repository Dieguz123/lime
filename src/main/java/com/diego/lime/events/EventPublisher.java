package com.diego.lime.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
public class EventPublisher {

    private static final Logger log = LoggerFactory.getLogger(EventPublisher.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final JsonMapper jsonMapper;

    public EventPublisher(KafkaTemplate<String, String> kafkaTemplate, JsonMapper jsonMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.jsonMapper = jsonMapper;
    }

    public void publishFilmImported(FilmImportedEvent event) {
        send(Topics.FILMS, event.filmId().toString(), event);
    }

    public void publishSwipeRecorded(SwipeRecordedEvent event) {
        send(Topics.SWIPES, event.roomId().toString(), event);
    }

    private void send(String topic, String key, Object event) {
        try {
            String json = jsonMapper.writeValueAsString(event);
            kafkaTemplate.send(topic, key, json)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.warn("Failed to publish event to {}: {}", topic, ex.getMessage());
                        }
                    });
        } catch (Exception e) {
            log.warn("Failed to publish event to {}: {}", topic, e.getMessage());
        }
    }
}