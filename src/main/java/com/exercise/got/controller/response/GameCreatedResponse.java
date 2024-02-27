package com.exercise.got.controller.response;

import com.exercise.got.enums.GameState;

public record GameCreatedResponse(Long id,
                                  PlayerResponse player1,
                                  PlayerResponse player2,
                                  Integer startingNumber,
                                  GameState gameState) {
}
