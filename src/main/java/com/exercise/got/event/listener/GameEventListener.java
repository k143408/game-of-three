package com.exercise.got.event.listener;


import com.exercise.got.business.GameActions;
import com.exercise.got.domain.Game;
import com.exercise.got.event.GameUpdateEvent;
import com.exercise.got.event.MoveMadeEvent;
import com.exercise.got.event.publisher.GameEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.exercise.got.enums.GameState.IN_PROGRESS;

@Component
@RequiredArgsConstructor
public class GameEventListener {
    private final GameEventPublisher gameEventPublisher;
    private final GameActions gameActions;

    @EventListener
    public void handleGameUpdateEvent(GameUpdateEvent event) {
        if (gameActions.isGameCompleted(event.getGame())) {
            gameEventPublisher.publishGameEndedEvent(event.getGame());
            return;
        }
        if (gameActions.isMovePossible(event.getGame())) {
            event.getGame().setGameState(IN_PROGRESS);
            Integer move = gameActions.generateNextMove(event.getGame().getCurrentPlayer(), null);
            gameEventPublisher.publishMoveMadeEvent(event.getGame(), move);
        }
    }

    @EventListener
    public void handleMoveMadeEvent(MoveMadeEvent event) {
        Game game = gameActions.performMove(event.getGame(), event.getGame().getCurrentPlayer(), event.getMove());
        gameEventPublisher.publishSaveGameEvent(game);
        gameEventPublisher.publishGameUpdateEvent(game);
    }
}
