package com.exercise.got.controller;


import com.exercise.got.controller.request.PlayerCreateRequest;
import com.exercise.got.controller.response.PlayerResponse;
import com.exercise.got.domain.Player;
import com.exercise.got.service.PlayerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PlayerController {
    private final PlayerService playerService;
    private final ObjectMapper objectMapper;
    @PostMapping
    public ResponseEntity<PlayerResponse> createUser(@RequestBody @Validated PlayerCreateRequest playerCreateRequest) {
        log.debug("Creating user: {}", playerCreateRequest.name());
        Player player = playerService.createUser(playerCreateRequest);
        log.info("User created successfully: id: {}, name: {}", player.getId(),player.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(objectMapper.convertValue(player, PlayerResponse.class));
    }
}
