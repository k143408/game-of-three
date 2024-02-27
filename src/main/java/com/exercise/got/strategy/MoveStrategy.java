package com.exercise.got.strategy;

import com.exercise.got.domain.Game;
import com.exercise.got.domain.Player;

public interface MoveStrategy {
    Integer generateMove(Integer userInput);
}
