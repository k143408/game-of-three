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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/games")
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<GameCreatedResponse> createGame(@RequestBody @Validated GameCreateRequest gameCreateRequest) {
        Game game = gameService.createGame(gameCreateRequest.players());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(objectMapper.convertValue(game, GameCreatedResponse.class));
    }

    @PatchMapping("/{gameId}/join")
    public ResponseEntity<?> joinGame(@PathVariable Long gameId, @RequestBody @Validated GameJoinRequest gameJoinRequest) {
        gameService.getJoinGame(gameId, gameJoinRequest.playerId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{gameId}/moves")
    public ResponseEntity<?> makeMove(@PathVariable Long gameId, @RequestBody @Validated MoveRequest moveRequest) {
        gameService.makeMove(gameId, moveRequest.playerId(), moveRequest.move());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<CurrentGameStateResponse> getGameById(@PathVariable Long gameId) {
        Game game = gameService.getGameById(gameId);
        return ResponseEntity.ok(objectMapper.convertValue(game, CurrentGameStateResponse.class));
    }
}
