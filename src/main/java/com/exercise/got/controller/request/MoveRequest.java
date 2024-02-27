package com.exercise.got.controller.request;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public record MoveRequest(
        @NotNull(message = "Player ID cannot be null")
        Long playerId,

        @Range(min = -1, max = 1)
        Integer move
) {
}
