package com.exercise.got.event;

import com.exercise.got.domain.Game;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class GameCreatedEvent extends ApplicationEvent {
    private final Game game;

    public GameCreatedEvent(Object source, Game game) {
        super(source);
        this.game = game;
    }
}
