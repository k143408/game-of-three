package com.exercise.got.strategy.impl;

import com.exercise.got.strategy.MoveStrategy;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomMoveStrategy implements MoveStrategy {
    @Override
    public Integer generateMove(Integer userInput) {
        return new Random().nextInt(3) - 1;
    }
}
