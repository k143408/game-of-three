package com.exercise.got.service.impl;

import com.exercise.got.controller.request.PlayerCreateRequest;
import com.exercise.got.domain.Player;
import com.exercise.got.enums.PlayerType;
import com.exercise.got.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.exercise.got.enums.PlayerType.AUTOMATIC;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PlayerServiceImplTest {

    public static final String PLAYER_NAME = "Jane Doe";
    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerServiceImpl playerService;

    @Test
    void testCreateUser() {
        PlayerCreateRequest request = new PlayerCreateRequest(PLAYER_NAME, AUTOMATIC);
        Player savedPlayer = Player.builder().id(1L).name(PLAYER_NAME).type(AUTOMATIC).build();

        when(playerRepository.save(any())).thenReturn(savedPlayer);

        Player createdPlayer = playerService.createUser(request);

        verify(playerRepository, times(1)).save(any());
        verifyNoMoreInteractions(playerRepository);

        assertNotNull(createdPlayer.getId());
        assertEquals(PLAYER_NAME, createdPlayer.getName());
        assertEquals(AUTOMATIC, createdPlayer.getType());
    }

    @Test
    void testGetPlayerById() {
        Long playerId = 1L;
        Player existingPlayer = Player.builder().id(playerId).name(PLAYER_NAME).type(AUTOMATIC).build();


        when(playerRepository.findById(eq(playerId))).thenReturn(Optional.of(existingPlayer));


        Player retrievedPlayer = playerService.getPlayerById(playerId);


        verify(playerRepository).findById(eq(playerId));
        verifyNoMoreInteractions(playerRepository);

        assertEquals(existingPlayer, retrievedPlayer);
    }

    @Test
    void testGetPlayersByIds() {
        List<Long> playerIds = List.of(1L, 2L);
        Player player1 = Player.builder().id(1L).name(PLAYER_NAME).type(AUTOMATIC).build();
        Player player2 = Player.builder().id(2L).name("Jane Doe").type(PlayerType.MANUAL).build();

        when(playerRepository.findById(eq(1L))).thenReturn(Optional.of(player1));
        when(playerRepository.findById(eq(2L))).thenReturn(Optional.of(player2));

        List<Player> retrievedPlayers = playerService.getPlayersByIds(playerIds);

        verify(playerRepository, times(1)).findById(eq(1L));
        verify(playerRepository, times(1)).findById(eq(2L));
        verifyNoMoreInteractions(playerRepository);

        assertEquals(2, retrievedPlayers.size());
        assertEquals(player1, retrievedPlayers.get(0));
        assertEquals(player2, retrievedPlayers.get(1));
    }

    @Test
    void testUpdatePlayer() {
        Long playerId = 1L;
        PlayerType newType = PlayerType.MANUAL;
        Player existingPlayer = Player.builder().id(playerId).name(PLAYER_NAME).type(AUTOMATIC).build();
        Player updatedPlayer = Player.builder().id(playerId).name(PLAYER_NAME).type(newType).build();

        when(playerRepository.findById(eq(playerId))).thenReturn(Optional.of(existingPlayer));
        when(playerRepository.save(any(Player.class))).thenReturn(updatedPlayer);

        Player resultPlayer = playerService.updatePlayer(playerId, newType);

        verify(playerRepository, times(1)).findById(eq(playerId));
        verifyNoMoreInteractions(playerRepository);

        assertEquals(updatedPlayer, resultPlayer);
    }

    @Test
    void testAcquirePlayer() {
        List<Player> players = List.of(
                Player.builder().id(1L).name(PLAYER_NAME).type(AUTOMATIC).build(),
                Player.builder().id(2L).name("Jon").type(PlayerType.MANUAL).build()
        );
        playerService.acquirePlayer(players);

        verify(playerRepository, times(1)).saveAll(eq(players));
        verifyNoMoreInteractions(playerRepository);
    }

    @Test
    void testReleasePlayer() {
        List<Player> players = List.of(
                Player.builder().id(1L).name(PLAYER_NAME).type(AUTOMATIC).available(false).build(),
                Player.builder().id(2L).name("Jon").type(PlayerType.MANUAL).available(false).build()
        );

        playerService.releasePlayer(players);

        verify(playerRepository, times(1)).saveAll(eq(players));
        verifyNoMoreInteractions(playerRepository);
    }
}
