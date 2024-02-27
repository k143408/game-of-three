package com.exercise.got.event;

import com.exercise.got.domain.Game;
import com.exercise.got.event.publisher.GameEventPublisher;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class GameSaveEvent extends ApplicationEvent {
    private final Game game;

    public GameSaveEvent(Object source, Game game) {
        super(source);
        this.game = game;
    }
}
