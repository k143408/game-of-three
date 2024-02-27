package com.exercise.got.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;


public record GameCreateRequest(
        @NotEmpty(message = "Players list cannot be empty")
        @Size(min = 1, max = 2, message = "Maximum 1 or 2 Players are allowed")
        List<Long> players
) {
}
