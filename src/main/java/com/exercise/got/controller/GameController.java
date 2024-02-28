package com.exercise.got.controller;


import com.exercise.got.controller.request.GameCreateRequest;
import com.exercise.got.controller.request.GameJoinRequest;
import com.exercise.got.controller.request.MoveRequest;
import com.exercise.got.controller.response.CurrentGameStateResponse;
import com.exercise.got.controller.response.GameCreatedResponse;
import com.exercise.got.domain.Game;
import com.exercise.got.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/games")
@RequiredArgsConstructor
@Slf4j
public class GameController {
    private final GameService gameService;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<GameCreatedResponse> createGame(@RequestBody @Validated GameCreateRequest gameCreateRequest) {
        log.debug("Received request to create a new game with players: {}", gameCreateRequest.players());
        Game game = gameService.createGame(gameCreateRequest.players());
        log.info("Game created with ID: {}", game.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(objectMapper.convertValue(game, GameCreatedResponse.class));
    }

    @PatchMapping("/{gameId}/join")
    public ResponseEntity<?> joinGame(@PathVariable Long gameId, @RequestBody @Validated GameJoinRequest gameJoinRequest) {
        log.debug("Player with ID {} is joining game with ID: {}", gameJoinRequest.playerId(), gameId);
        gameService.getJoinGame(gameId, gameJoinRequest.playerId());
        log.info("Player joined successfully.");
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{gameId}/moves")
    public ResponseEntity<?> makeMove(@PathVariable Long gameId, @RequestBody @Validated MoveRequest moveRequest) {
        log.debug("Received move request for game with ID {} from player ID {}: {}", gameId, moveRequest.playerId(), moveRequest.move());
        gameService.makeMove(gameId, moveRequest.playerId(), moveRequest.move());
        log.info("Move processed successfully.");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<CurrentGameStateResponse> getGameById(@PathVariable Long gameId) {
        log.info("Received request to get game state for game with ID: {}", gameId);
        Game game = gameService.getGameById(gameId);
        return ResponseEntity.ok(objectMapper.convertValue(game, CurrentGameStateResponse.class));
    }
}
