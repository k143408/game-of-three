package com.exercise.got.strategy.impl;

import com.exercise.got.domain.Game;
import com.exercise.got.domain.Player;
import com.exercise.got.strategy.MoveStrategy;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class ManualMoveStrategy implements MoveStrategy{
    @Override
    public Integer generateMove(Integer userInput) {
        return userInput;
    }
}
