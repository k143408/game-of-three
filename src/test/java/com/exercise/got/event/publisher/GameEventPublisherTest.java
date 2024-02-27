package com.exercise.got.event.publisher;

import com.exercise.got.domain.Game;
import com.exercise.got.event.GameEndedEvent;
import com.exercise.got.event.GameSaveEvent;
import com.exercise.got.event.GameUpdateEvent;
import com.exercise.got.event.MoveMadeEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class GameEventPublisherTest {

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private GameEventPublisher gameEventPublisher;

    @Test
    void testPublishMoveMadeEvent() {
        Game game = new Game();
        Integer move = 2;

        gameEventPublisher.publishMoveMadeEvent(game, move);
        verify(eventPublisher).publishEvent(any(MoveMadeEvent.class));
    }

    @Test
    void testPublishGameUpdateEvent() {
        Game game = new Game();
        
        gameEventPublisher.publishGameUpdateEvent(game);
        verify(eventPublisher).publishEvent(any(GameUpdateEvent.class));
    }

    @Test
    void testPublishGameEndedEvent() {
        Game game = new Game();
        gameEventPublisher.publishGameEndedEvent(game);

        verify(eventPublisher).publishEvent(any(GameEndedEvent.class));
    }

    @Test
    void testPublishSaveGameEvent() {
        Game game = new Game();

        gameEventPublisher.publishSaveGameEvent(game);
        verify(eventPublisher).publishEvent(any(GameSaveEvent.class));
    }
}

