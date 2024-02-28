package com.exercise.got.service.impl;

import com.exercise.got.business.GameActions;
import com.exercise.got.domain.Game;
import com.exercise.got.domain.Player;
import com.exercise.got.event.GameEndedEvent;
import com.exercise.got.event.GameSaveEvent;
import com.exercise.got.event.publisher.GameEventPublisher;
import com.exercise.got.repository.GameRepository;
import com.exercise.got.service.GameService;
import com.exercise.got.service.PlayerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.exercise.got.enums.GameState.CREATED;
import static com.exercise.got.enums.GameState.WAITING_FOR_ANOTHER_PLAYER;
import static com.exercise.got.enums.PlayerType.MANUAL;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameServiceImpl implements GameService {
    private final PlayerService playerService;
    private final GameRepository gameRepository;
    private final GameEventPublisher gameEventPublisher;
    private final GameActions gameActions;


    @Override
    public Game createGame(List<Long> playerIds) {
        List<Player> players = playerService.getPlayersByIds(playerIds);
        Game game;
        if (players.size() == 2) {
            game = gameActions.createGameForTwoPlayers(players);
        } else {
            game = gameActions.createGameWaitingForAnotherPlayer(players);
        }
        playerService.acquirePlayer(players);
        gameEventPublisher.publishGameUpdateEvent(game);
        return game;
    }

    @Override
    public void getJoinGame(Long gameId, Long playerId) {
        Player player2 = playerService.getPlayerById(playerId);
        Game game = gameRepository.findById(gameId).orElseThrow(EntityNotFoundException::new);
        if (game.getGameState() == WAITING_FOR_ANOTHER_PLAYER) {
            game.setPlayer2(player2);
            game.setGameState(CREATED);
            gameEventPublisher.publishGameUpdateEvent(game);
            log.info("Player {} joined game {}", player2.getName(), gameId);
        }
    }

    @Override
    public void makeMove(Long gameId, Long playerId, Integer move) {
        Player player = playerService.getPlayerById(playerId);
        if (player.getType() == MANUAL) {
            Game game = gameRepository.findById(gameId).orElseThrow(EntityNotFoundException::new);
            if (gameActions.isDividedByValue(move, game.getCurrentNumber())) {
                gameEventPublisher.publishMoveMadeEvent(game, move);
                return;
            }
            throw new IllegalArgumentException("Move is not divisible by 3");
        }
        throw new UnsupportedOperationException("Player type is Automatic");
    }

    @Override
    public Game getGameById(Long gameId) {
        return gameRepository.findById(gameId).orElseThrow(EntityNotFoundException::new);
    }

    @EventListener
    public void save(GameSaveEvent event) {
        gameRepository.save(event.getGame());
    }

    @EventListener
    public void handleGameEndedEvent(GameEndedEvent event) {
        Game game = gameActions.endGame(event.getGame());
        playerService.releasePlayer(List.of(game.getPlayer1(), game.getPlayer2()));
        gameEventPublisher.publishSaveGameEvent(game);
    }
}
