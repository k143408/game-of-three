package com.exercise.got.controller.response;

import com.exercise.got.enums.GameState;

public record CurrentGameStateResponse(PlayerResponse player1,
                                       PlayerResponse player2,
                                       Integer currentNumber,
                                       PlayerResponse currentPlayer,
                                       GameState gameState) {
}
