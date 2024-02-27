package com.exercise.got.business;

import com.exercise.got.domain.Game;
import com.exercise.got.domain.Player;
import com.exercise.got.enums.GameState;
import com.exercise.got.enums.PlayerType;
import com.exercise.got.factory.MoveStrategyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameActionsTest {

    @Mock
    private MoveStrategyFactory moveStrategyFactory;

    @InjectMocks
    private GameActions gameActions;

    @BeforeEach
    public void init() {
        ReflectionTestUtils.setField(gameActions, "startNumberRange", 1000);
        ReflectionTestUtils.setField(gameActions, "mod", 3);
    }

    @Test
    void testCreateGameForTwoPlayers() {
        Player player1 = new Player(1L, "Player1", true, PlayerType.AUTOMATIC);
        Player player2 = new Player(2L, "Player2", true, PlayerType.AUTOMATIC);
        List<Player> players = List.of(player1, player2);

        Game game = gameActions.createGameForTwoPlayers(players);

        assertNotNull(game);
        assertEquals(player1, game.getPlayer1());
        assertEquals(player2, game.getPlayer2());
        assertEquals(player1, game.getCurrentPlayer());
        assertEquals(GameState.CREATED, game.getGameState());
        assertNotNull(game.getStartingNumber());
    }

    @Test
    void testCreateGameWaitingForAnotherPlayer() {
        Player player1 = new Player(1L, "Player1", true, PlayerType.AUTOMATIC);
        List<Player> players = List.of(player1);

        Game game = gameActions.createGameWaitingForAnotherPlayer(players);

        assertNotNull(game);
        assertEquals(player1, game.getPlayer1());
        assertEquals(player1, game.getCurrentPlayer());
        assertEquals(GameState.WAITING_FOR_ANOTHER_PLAYER, game.getGameState());
        assertNotNull(game.getStartingNumber());
    }

    @Test
    void testIsMovePossible() {
        Game game1 = new Game();
        game1.setGameState(GameState.CREATED);
        game1.setCurrentPlayer(new Player(1L, "Player1", true, PlayerType.AUTOMATIC));

        Game game2 = new Game();
        game2.setGameState(GameState.WAITING_FOR_ANOTHER_PLAYER);
        game2.setCurrentPlayer(new Player(1L, "Player1", true, PlayerType.AUTOMATIC));

        Game game3 = new Game();
        game3.setGameState(GameState.CREATED);
        game3.setCurrentPlayer(new Player(1L, "Player1", true, PlayerType.MANUAL));

        assertTrue(gameActions.isMovePossible(game1));
        assertFalse(gameActions.isMovePossible(game2));
        assertFalse(gameActions.isMovePossible(game3));
    }

    @Test
    void testGenerateNextMove() {
        Player player = new Player(1L, "Player1", true, PlayerType.AUTOMATIC);
        int move = 0;

        when(moveStrategyFactory.getStrategy(PlayerType.AUTOMATIC)).thenReturn(moveStrategy -> move + 1);

        int generatedMove = gameActions.generateNextMove(player, move);

        assertEquals(move + 1, generatedMove);
    }

    @Test
    void testIsValidMove_True() {
        Integer move = 1;
        boolean result = gameActions.isValidMove(move);

        assertTrue(result);
    }

    @Test
    void testIsValidMove_False() {
        Integer move = 2;

        boolean result = gameActions.isValidMove(move);

        assertFalse(result);
    }

    @Test
    void testGetOpponent() {
        Game game = new Game();
        Player player1 = new Player(1L, "Player1", true, PlayerType.AUTOMATIC);
        Player player2 = new Player(2L, "Player2", true, PlayerType.AUTOMATIC);
        game.setPlayer1(player1);
        game.setPlayer2(player2);

        Player opponent = gameActions.getOpponent(game, player1.getId());

        assertEquals(player2, opponent);
    }


    @Test
    void testIsGameCompleted_True() {
        Game game = new Game();
        game.setCurrentNumber(1);
        Player player1 = new Player(1L, "Player1", true, PlayerType.AUTOMATIC);
        game.setCurrentPlayer(player1);
        game.setPlayer1(player1);

        boolean result = gameActions.isGameCompleted(game);

        assertTrue(result);
    }

    @Test
    void testIsGameCompleted_False() {
        Game game = new Game();
        game.setCurrentNumber(2);
        game.setCurrentPlayer(new Player(1L, "Player1", true, PlayerType.AUTOMATIC));

        boolean result = gameActions.isGameCompleted(game);

        assertFalse(result);
    }

    @Test
    void testEndGame() {
        Game game = new Game();
        Player player1 = new Player(1L, "Player1", true, PlayerType.AUTOMATIC);
        Player player2 = new Player(2L, "Player2", true, PlayerType.AUTOMATIC);
        game.setCurrentPlayer(player1);
        game.setPlayer1(player1);
        game.setPlayer2(player2);

        Game endedGame = gameActions.endGame(game);

        assertEquals(GameState.FINISHED, endedGame.getGameState());
        assertEquals(player2, endedGame.getWinner());
    }
}

