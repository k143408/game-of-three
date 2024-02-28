package com.exercise.got.event;

import com.exercise.got.domain.Game;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;

@Getter
@Slf4j
public class GameEndedEvent extends ApplicationEvent {
    private final Game game;

    public GameEndedEvent(Object source, Game game) {
        super(source);
        this.game = game;
        log.debug("GameEndedEvent created for game with ID: {}", game.getId());
    }
}
