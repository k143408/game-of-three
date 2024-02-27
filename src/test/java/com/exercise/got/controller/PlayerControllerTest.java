package com.exercise.got.controller;

import com.exercise.got.JsonUtil;
import com.exercise.got.controller.request.PlayerCreateRequest;
import com.exercise.got.controller.response.PlayerResponse;
import com.exercise.got.domain.Player;
import com.exercise.got.enums.PlayerType;
import com.exercise.got.service.PlayerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.exercise.got.enums.PlayerType.AUTOMATIC;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class PlayerControllerTest {

    public static final String PLAYER_NAME = "John Doe";
    private MockMvc mockMvc;

    @Mock
    private PlayerService playerService;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() {
        PlayerController playerController = new PlayerController(playerService, objectMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(playerController).build();
    }

    @Test
    void testCreateUser() throws Exception {
        PlayerCreateRequest request = new PlayerCreateRequest(PLAYER_NAME, AUTOMATIC);
        Player player = new Player(1L, PLAYER_NAME, true, AUTOMATIC);
        PlayerResponse playerResponse = new PlayerResponse(1L, PLAYER_NAME, AUTOMATIC);


        when(playerService.createUser(any(PlayerCreateRequest.class))).thenReturn(player);
        when(objectMapper.convertValue(any(), eq(PlayerResponse.class))).thenReturn(playerResponse);

        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(PLAYER_NAME))
                .andExpect(jsonPath("$.type").value(AUTOMATIC.name()));

        verify(playerService, times(1)).createUser(eq(request));
        verify(objectMapper, times(1)).convertValue(eq(player), eq(PlayerResponse.class));
        verifyNoMoreInteractions(playerService, objectMapper);
    }
}
