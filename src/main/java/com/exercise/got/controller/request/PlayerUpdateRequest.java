package com.exercise.got.controller.request;

import com.exercise.got.enums.PlayerType;

public record PlayerUpdateRequest(Long id, PlayerType type) {
}
