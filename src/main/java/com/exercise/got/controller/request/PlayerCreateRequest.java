package com.exercise.got.controller.request;

import com.exercise.got.enums.PlayerType;
import jakarta.validation.constraints.NotBlank;


public record PlayerCreateRequest(@NotBlank String name, PlayerType type) { }
