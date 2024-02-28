package com.exercise.got.service.impl;

import com.exercise.got.controller.request.PlayerCreateRequest;
import com.exercise.got.domain.Player;
import com.exercise.got.enums.PlayerType;
import com.exercise.got.repository.PlayerRepository;
import com.exercise.got.service.PlayerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;

    @Override
    public Player createUser(PlayerCreateRequest request) {
        Player.PlayerBuilder builder = Player.builder();
        if (request.type() != null) {
            builder.type(request.type());
        }
        Player player = builder.name(request.name()).build();
        log.debug("Player name {} is created", player.getName());
        return playerRepository.save(player);
    }

    @Override
    public Player getPlayerById(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Player does not exist against id %d", id)));
    }

    @Override
    public List<Player> getPlayersByIds(List<Long> playerIds) {
        return playerIds.stream().map(this::getPlayerById).toList();
    }

    @Override
    public Player updatePlayer(Long id, PlayerType type) {
        Player player = getPlayerById(id);
        player.setType(type);
        log.debug("Player id {} : type is changed to {}", player.getId(), type);
        return playerRepository.save(player);
    }

    @Override
    public void acquirePlayer(List<Player> players) {
        players.forEach(p -> {
            log.debug("Acquire player id {}", p.getId());
            p.setAvailable(false);
        });
        playerRepository.saveAll(players);
    }

    @Override
    public void releasePlayer(List<Player> players) {
        players.forEach(p -> {
            log.debug("Release player id {}", p.getId());
            p.setAvailable(true);
        });
        playerRepository.saveAll(players);
    }
}
