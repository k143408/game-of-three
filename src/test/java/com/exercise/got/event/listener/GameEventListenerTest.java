package com.exercise.got.event.listener;

import com.exercise.got.business.GameActions;
import com.exercise.got.domain.Game;
import com.exercise.got.enums.GameState;
import com.exercise.got.event.GameUpdateEvent;
import com.exercise.got.event.publisher.GameEventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameEventListenerTest {

    @Mock
    private GameEventPublisher gameEventPublisher;

    @Mock
    private GameActions gameActions;

    @InjectMocks
    private GameEventListener gameEventListener;

    @Test
    void testHandleGameUpdateEvent_GameCompleted() {
        GameUpdateEvent event = new GameUpdateEvent(this, createGame(GameState.FINISHED));

        when(gameActions.isGameCompleted(any())).thenReturn(true);

        gameEventListener.handleGameUpdateEvent(event);

        verify(gameEventPublisher, times(1)).publishGameEndedEvent(eq(event.getGame()));
        verifyNoMoreInteractions(gameEventPublisher, gameActions);
    }

    @Test
    void testHandleGameUpdateEvent_MovePossible() {
        GameUpdateEvent event = new GameUpdateEvent(this, mock(Game.class));

        when(gameActions.isGameCompleted(any())).thenReturn(false);
        when(gameActions.isMovePossible(any())).thenReturn(true);
        when(gameActions.generateNextMove(any(), any())).thenReturn(1);

        gameEventListener.handleGameUpdateEvent(event);

        verify(gameActions, times(1)).generateNextMove(eq(event.getGame().getCurrentPlayer()), eq(null));
        verify(gameEventPublisher, times(1)).publishMoveMadeEvent(eq(event.getGame()), eq(1));

        verifyNoMoreInteractions(gameEventPublisher, gameActions);
    }

    @Test
    void testHandleGameUpdateEvent_MoveNotPossible() {
        GameUpdateEvent event = new GameUpdateEvent(this, createGame(GameState.CREATED));

        when(gameActions.isGameCompleted(any())).thenReturn(false);
        when(gameActions.isMovePossible(any())).thenReturn(false);

        gameEventListener.handleGameUpdateEvent(event);

        verifyNoInteractions(gameEventPublisher);
    }


    private Game createGame(GameState gameState) {
        return Game.builder().gameState(gameState).build();
    }
}

