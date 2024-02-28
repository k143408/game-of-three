package com.exercise.got.event;

import com.exercise.got.domain.Game;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;

@Getter
@Slf4j
public class MoveMadeEvent extends ApplicationEvent {

    private final Game game;
    private final Integer move;

    public MoveMadeEvent(Object source, Game game, Integer move) {
        super(source);
        this.game = game;
        this.move = move;
        log.debug("MoveMadeEvent created for game with ID: {} and move: {}", game.getId(), move);
    }
}
