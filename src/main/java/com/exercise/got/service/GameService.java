package com.exercise.got.service;

import com.exercise.got.domain.Game;

import java.util.List;

public interface GameService {
    Game createGame(List<Long> players);

    void getJoinGame(Long gameId, Long playerId);

    void makeMove(Long gameId, Long playerId, Integer move);

    Game getGameById(Long gameId);
}
