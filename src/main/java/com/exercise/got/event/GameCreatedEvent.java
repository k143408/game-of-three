package com.exercise.got.event;

import com.exercise.got.domain.Game;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;

@Getter
@Slf4j
public class GameCreatedEvent extends ApplicationEvent {
    private final Game game;

    public GameCreatedEvent(Object source, Game game) {
        super(source);
        this.game = game;
        log.debug("GameCreatedEvent created for game with ID: {}", game.getId());
    }
}
