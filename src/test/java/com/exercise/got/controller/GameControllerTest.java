package com.exercise.got.controller;

import com.exercise.got.JsonUtil;
import com.exercise.got.controller.request.GameCreateRequest;
import com.exercise.got.controller.request.GameJoinRequest;
import com.exercise.got.controller.request.MoveRequest;
import com.exercise.got.controller.response.GameCreatedResponse;
import com.exercise.got.controller.response.PlayerResponse;
import com.exercise.got.domain.Game;
import com.exercise.got.enums.GameState;
import com.exercise.got.enums.PlayerType;
import com.exercise.got.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class GameControllerTest {
    private MockMvc mockMvc;
    @Mock
    private GameService gameService;
    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() {
        GameController gameController = new GameController(gameService, objectMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(gameController).build();
    }

    @Test
    void testCreateGame() throws Exception {
        GameCreateRequest request = new GameCreateRequest(List.of(1L, 2L));
        Game game = new Game();
        PlayerResponse player1 = new PlayerResponse(1L, "1", PlayerType.AUTOMATIC);
        PlayerResponse player2 = new PlayerResponse(2L, "2", PlayerType.AUTOMATIC);
        GameCreatedResponse gameCreatedResponse = new GameCreatedResponse(1L, player1, player2, 86, GameState.CREATED);

        when(gameService.createGame(anyList())).thenReturn(game);

        when(objectMapper.convertValue(any(), eq(GameCreatedResponse.class))).thenReturn(gameCreatedResponse);

        mockMvc.perform(post("/api/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.player1.id").exists())
                .andExpect(jsonPath("$.player2.id").exists())
                .andExpect(jsonPath("$.startingNumber").exists())
                .andExpect(jsonPath("$.gameState").value("CREATED"));
    }

    @Test
    void testJoinGame() throws Exception {
        Long gameId = 1L;
        Long playerId = 3L;
        GameJoinRequest request = new GameJoinRequest(playerId);

        doNothing().when(gameService).getJoinGame(eq(gameId), eq(playerId));

        mockMvc.perform(patch("/api/games/{gameId}/join", gameId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(request)))
                .andExpect(status().isOk());

        verify(gameService).getJoinGame(eq(gameId), eq(playerId));
    }

    @Test
    void testMakeMove() throws Exception {
        Long gameId = 1L;
        Long playerId = 1L;
        MoveRequest request = new MoveRequest(playerId, 1);

        doNothing().when(gameService).makeMove(eq(gameId), eq(playerId), eq(1));

        mockMvc.perform(put("/api/games/{gameId}/moves", gameId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(request)))
                .andExpect(status().isOk());

        verify(gameService, times(1)).makeMove(eq(gameId), eq(playerId), eq(1));
    }

    @Test
    void testGetGameById() throws Exception {
        Long gameId = 1L;

        when(gameService.getGameById(eq(gameId))).thenReturn(mock(Game.class));

        mockMvc.perform(get("/api/games/{gameId}", gameId))
                .andExpect(status().isOk());

        verify(gameService, times(1)).getGameById(eq(gameId));
    }
}
