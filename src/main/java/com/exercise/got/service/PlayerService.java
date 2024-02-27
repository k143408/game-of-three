package com.exercise.got.service;

import com.exercise.got.controller.request.PlayerCreateRequest;
import com.exercise.got.domain.Player;
import com.exercise.got.enums.PlayerType;

import java.util.List;

public interface PlayerService {
    Player createUser(PlayerCreateRequest playerCreateRequest);

    Player getPlayerById(Long id);

    List<Player> getPlayersByIds(List<Long> playerIds);

    Player updatePlayer(Long id, PlayerType type);

    void acquirePlayer(List<Player> players);

    void releasePlayer(List<Player> players);
}
