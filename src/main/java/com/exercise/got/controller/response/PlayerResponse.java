package com.exercise.got.controller.response;

import com.exercise.got.enums.PlayerType;

public record PlayerResponse(Long id, String name, PlayerType type) {
}
