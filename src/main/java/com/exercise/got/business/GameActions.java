package com.exercise.got.business;

import com.exercise.got.domain.Game;
import com.exercise.got.enums.GameState;
import com.exercise.got.domain.Player;
import com.exercise.got.enums.PlayerType;
import com.exercise.got.factory.MoveStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class GameActions {
    private final MoveStrategyFactory moveStrategyFactory;

    @Value("${game.config.startingNumberRange:100000}")
    private Integer startNumberRange;
    @Value("${game.config.mod:3}")
    private Integer mod;

    public Game createGameForTwoPlayers(List<Player> players) {
        log.info("Creating game for two players");
        var player1 = players.getFirst();
        var player2 = players.getLast();

        return Game.builder()
                .player1(player1)
                .player2(player2)
                .currentPlayer(player1)
                .gameState(GameState.CREATED)
                .startingNumber(new Random().nextInt(startNumberRange))
                .build();
    }

    public Game createGameWaitingForAnotherPlayer(List<Player> players) {
        log.info("Creating game waiting for another player");
        var player1 = players.getFirst();

        return Game.builder()
                .player1(player1)
                .currentPlayer(player1)
                .gameState(GameState.WAITING_FOR_ANOTHER_PLAYER)
                .startingNumber(new Random().nextInt(startNumberRange))
                .build();
    }

    public boolean isMovePossible(Game game) {
        log.debug("Checking if move is possible for game with ID: {}", game.getId());
        return game.getGameState() != GameState.WAITING_FOR_ANOTHER_PLAYER
                && game.getCurrentPlayer().getType() == PlayerType.AUTOMATIC;
    }

    public Game performMove(Game game, Player player, Integer move) {
        if (isValidMove(move)) {
            var currentNumber = game.getCurrentNumber() != null ? game.getCurrentNumber() : game.getStartingNumber();
            if (isDividedByValue(move, currentNumber, mod)) {
                var newNumber = (currentNumber + move) / mod;
                log.info("Player {} made a move: added {} in current {} resulting number: {}", player.getName(), move, currentNumber, newNumber);
                game.setCurrentNumber(newNumber);
                game.setCurrentPlayer(getOpponent(game, player.getId()));
            }
        }
        return game;
    }

    public boolean isDividedByValue(int move, int current) {
        log.debug("Checking if move {} is divisible by current {}", move, current);
        return isDividedByValue(move, current, mod);
    }

    private boolean isDividedByValue(int move, int current, int mod) {
        boolean isDivisible = (move + current) % mod == 0;
        log.debug("Checking if move {} + current {} is divisible by mod {}: {}", move, current, mod, isDivisible);
        return isDivisible;
    }

    public Integer generateNextMove(Player player, Integer move) {
        log.debug("Generating next move for player {} with current move: {}", player.getName(), move);
        return moveStrategyFactory.getStrategy(player.getType()).generateMove(move);
    }

    public boolean isValidMove(Integer move) {
        log.debug("Checking if move {} is valid", move);
        return move != null && (move.equals(-1) || move.equals(0) || move.equals(1));
    }

    protected Player getOpponent(Game game, Long playerId) {
        return game.getPlayer1().getId().equals(playerId) ? game.getPlayer2() : game.getPlayer1();
    }
    public boolean isGameCompleted(Game game) {
        boolean isCompleted = game.getCurrentNumber() != null && game.getCurrentNumber().compareTo(1) == 0 || game.getCurrentPlayer().equals(game.getPlayer1());
        log.debug("Checking if game with ID {} is completed: {}", game.getId(), isCompleted);
        return isCompleted;
    }
    public Game endGame(Game game) {
        log.info("Ending game with ID: {}", game.getId());
        game.setGameState(GameState.FINISHED);
        game.setWinner(getOpponent(game, game.getCurrentPlayer().getId()));
        return game;
    }
}
