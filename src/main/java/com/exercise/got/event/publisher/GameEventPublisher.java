package com.exercise.got.event.publisher;

import com.exercise.got.domain.Game;
import com.exercise.got.event.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameEventPublisher {
    private final ApplicationEventPublisher eventPublisher;
    public void publishMoveMadeEvent(Game game, Integer move) {
        eventPublisher.publishEvent(new MoveMadeEvent(this,game, move));
    }

    public void publishGameUpdateEvent(Game game) {
        eventPublisher.publishEvent(new GameUpdateEvent(this,game));
    }

    public void publishGameEndedEvent(Game game) {
        eventPublisher.publishEvent(new GameEndedEvent(this,game));
    }

    public void publishSaveGameEvent(Game game) {
        eventPublisher.publishEvent(new GameSaveEvent(this,game));
    }
}
