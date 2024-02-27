package com.exercise.got.controller;


import com.exercise.got.controller.request.PlayerCreateRequest;
import com.exercise.got.controller.response.PlayerResponse;
import com.exercise.got.domain.Player;
import com.exercise.got.service.PlayerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/players")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService playerService;
    private final ObjectMapper objectMapper;
    @PostMapping
    public ResponseEntity<PlayerResponse> createUser(@RequestBody @Validated PlayerCreateRequest playerCreateRequest) {
        Player player = playerService.createUser(playerCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(objectMapper.convertValue(player, PlayerResponse.class));
    }
}
