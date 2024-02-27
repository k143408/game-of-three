package com.exercise.got.service.impl;

import com.exercise.got.business.GameActions;
import com.exercise.got.domain.Game;
import com.exercise.got.domain.Player;
import com.exercise.got.enums.GameState;
import com.exercise.got.enums.PlayerType;
import com.exercise.got.event.publisher.GameEventPublisher;
import com.exercise.got.repository.GameRepository;
import com.exercise.got.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.exercise.got.enums.PlayerType.MANUAL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceImplTest {

    @Mock
    private PlayerService playerService;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameEventPublisher gameEventPublisher;

    @Mock
    private GameActions gameActions;

    @InjectMocks
    private GameServiceImpl gameService;

    @Test
    void testCreateGameForTwoPlayers() {
        List<Long> playerIds = List.of(1L, 2L);
        Player player1 = new Player(1L, "Player1", true, MANUAL);
        Player player2 = new Player(2L, "Player2", true, MANUAL);
        Game game = new Game();

        when(playerService.getPlayersByIds(eq(playerIds))).thenReturn(Arrays.asList(player1, player2));
        when(gameActions.createGameForTwoPlayers(anyList())).thenReturn(game);

        Game createdGame = gameService.createGame(playerIds);

        verify(playerService, times(1)).getPlayersByIds(eq(playerIds));
        verify(gameActions, times(1)).createGameForTwoPlayers(anyList());
        verify(playerService, times(1)).acquirePlayer(anyList());
        verify(gameEventPublisher, times(1)).publishGameUpdateEvent(eq(game));
        verifyNoMoreInteractions(playerService, gameActions, gameEventPublisher);

        assertEquals(game, createdGame);
    }

    @Test
    void testCreateGameWaitingForAnotherPlayer() {
        
        List<Long> playerIds = List.of(1L);
        Player player1 = new Player(1L, "Player1", true, MANUAL);
        Game game = new Game();

        when(playerService.getPlayersByIds(eq(playerIds))).thenReturn(Arrays.asList(player1));
        when(gameActions.createGameWaitingForAnotherPlayer(anyList())).thenReturn(game);

        Game createdGame = gameService.createGame(playerIds);

        verify(playerService, times(1)).getPlayersByIds(eq(playerIds));
        verify(gameActions, times(1)).createGameWaitingForAnotherPlayer(anyList());
        verify(playerService, times(1)).acquirePlayer(anyList());
        verify(gameEventPublisher, times(1)).publishGameUpdateEvent(eq(game));
        verifyNoMoreInteractions(playerService, gameActions, gameEventPublisher);

        assertEquals(game, createdGame);
    }

    @Test
    void testGetJoinGame() {
        Long gameId = 1L;
        Long playerId = 2L;
        Player player2 = new Player(2L, "Player2", true, PlayerType.MANUAL);
        Game game = new Game();
        game.setId(gameId);
        game.setGameState(GameState.WAITING_FOR_ANOTHER_PLAYER);

        when(playerService.getPlayerById(eq(playerId))).thenReturn(player2);
        when(gameRepository.findById(eq(gameId))).thenReturn(Optional.of(game));

        gameService.getJoinGame(gameId, playerId);
        
        verify(playerService, times(1)).getPlayerById(eq(playerId));
        verify(gameRepository, times(1)).findById(eq(gameId));
        verify(gameEventPublisher, times(1)).publishGameUpdateEvent(eq(game));
        verifyNoMoreInteractions(playerService, gameRepository, gameEventPublisher);
    }

    @Test
    void testMakeMoveForManualPlayerValidMove() {
        Long gameId = 1L;
        Long playerId = 2L;
        Integer move = 2;
        Player player = new Player(playerId, "Player", true, PlayerType.MANUAL);
        Game game = new Game();
        game.setId(gameId);
        game.setCurrentNumber(3);
        
        when(playerService.getPlayerById(eq(playerId))).thenReturn(player);
        when(gameRepository.findById(eq(gameId))).thenReturn(Optional.of(game));
        when(gameActions.isDividedByValue(eq(move), eq(game.getCurrentNumber()))).thenReturn(true);
        gameService.makeMove(gameId, playerId, move);

        verify(playerService, times(1)).getPlayerById(eq(playerId));
        verify(gameRepository, times(1)).findById(eq(gameId));
        verify(gameEventPublisher, times(1)).publishMoveMadeEvent(eq(game), eq(move));
        verifyNoMoreInteractions(playerService, gameRepository, gameEventPublisher);
    }

    @Test
    void testMakeMoveForManualPlayerInvalidMove() {
        Long gameId = 1L;
        Long playerId = 2L;
        Integer move = 3;
        Player player = new Player(playerId, "Player", true, PlayerType.MANUAL);
        Game game = new Game();
        game.setId(gameId);
        game.setCurrentNumber(3);

        when(playerService.getPlayerById(eq(playerId))).thenReturn(player);
        when(gameRepository.findById(eq(gameId))).thenReturn(Optional.of(game));
        when(gameActions.isDividedByValue(eq(move), eq(game.getCurrentNumber()))).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> gameService.makeMove(gameId, playerId, move));

        verify(playerService, times(1)).getPlayerById(eq(playerId));
        verify(gameRepository, times(1)).findById(eq(gameId));
        verifyNoMoreInteractions(playerService, gameRepository, gameEventPublisher);
    }

    @Test
    void testMakeMoveForAutomaticPlayer() {
        Long gameId = 1L;
        Long playerId = 2L;
        Integer move = 2;
        Player player = new Player(playerId, "Player", true, PlayerType.AUTOMATIC);
        Game game = new Game();
        game.setId(gameId);

        when(playerService.getPlayerById(eq(playerId))).thenReturn(player);

        assertThrows(UnsupportedOperationException.class, () -> gameService.makeMove(gameId, playerId, move));

        verify(playerService, times(1)).getPlayerById(eq(playerId));

        verifyNoMoreInteractions(playerService, gameRepository, gameEventPublisher);
    }

    @Test
    void testGetGameById() {
        
        Long gameId = 1L;
        Game game = new Game();
        game.setId(gameId);
        when(gameRepository.findById(eq(gameId))).thenReturn(Optional.of(game));
        Game retrievedGame = gameService.getGameById(gameId);
        verify(gameRepository, times(1)).findById(eq(gameId));
        verifyNoMoreInteractions(playerService, gameRepository, gameEventPublisher);

        assertEquals(game, retrievedGame);
    }
}

