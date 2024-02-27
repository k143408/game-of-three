package com.exercise.got.event;

import com.exercise.got.domain.Game;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MoveMadeEvent extends ApplicationEvent {

    private final Game game;
    private final Integer move;

    public MoveMadeEvent(Object source, Game game, Integer move) {
        super(source);
        this.game = game;
        this.move = move;
    }
}
