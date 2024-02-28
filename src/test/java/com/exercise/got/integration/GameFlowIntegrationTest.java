package com.exercise.got.integration;

import com.exercise.got.controller.request.GameCreateRequest;
import com.exercise.got.controller.request.PlayerCreateRequest;
import com.exercise.got.controller.response.CurrentGameStateResponse;
import com.exercise.got.controller.response.GameCreatedResponse;
import com.exercise.got.controller.response.PlayerResponse;
import com.exercise.got.enums.PlayerType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class GameFlowIntegrationTest {
    @Container
    private static final GenericContainer<?> postgresContainer = new GenericContainer<>("postgres:13")
            .withExposedPorts(5432)
            .withEnv("POSTGRES_DB", "testdb")
            .withEnv("POSTGRES_USER", "testuser")
            .withEnv("POSTGRES_PASSWORD", "testpassword");
    Logger logger = Logger.getLogger(this.getClass().getName());
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGameFlow() throws Exception {
        ResultActions player1Result = createPlayer("Player1");

        Long player1Id = objectMapper.readValue(player1Result.andReturn().getResponse().getContentAsString(), PlayerResponse.class).id();

        ResultActions player2Result = createPlayer("Player2");

        Long player2Id = objectMapper.readValue(player2Result.andReturn().getResponse().getContentAsString(), PlayerResponse.class).id();

        ResultActions createGameResult = createGame(player1Id, player2Id);

        Long gameId = objectMapper.readValue(createGameResult.andReturn().getResponse().getContentAsString(), GameCreatedResponse.class).id();

        ResultActions getGameResult = mockMvc.perform(get("/api/games/{gameId}", gameId))
                .andExpect(status().isOk());

        PlayerResponse winner = objectMapper.readValue(getGameResult.andReturn().getResponse().getContentAsString(), CurrentGameStateResponse.class).currentPlayer();

        logger.info(() -> "Winner " + winner.name());
    }

    private ResultActions createPlayer(String playerName) throws Exception {
        String playerRequestBody = objectMapper.writeValueAsString(new PlayerCreateRequest(playerName, PlayerType.AUTOMATIC));
        return mockMvc.perform(post("/api/players")
                        .contentType("application/json")
                        .content(playerRequestBody))
                .andExpect(status().isCreated());
    }

    private ResultActions createGame(Long player1Id, Long player2Id) throws Exception {
        String gameRequestBody = objectMapper.writeValueAsString(new GameCreateRequest(List.of(player1Id, player2Id)));
        return mockMvc.perform(post("/api/games")
                        .contentType("application/json")
                        .content(gameRequestBody))
                .andExpect(status().isCreated());
    }
}
