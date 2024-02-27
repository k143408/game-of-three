package com.exercise.got.controller.request;

import jakarta.validation.constraints.NotNull;

public record GameJoinRequest(
        @NotNull(message = "Player ID cannot be null")
        Long playerId
) { }
