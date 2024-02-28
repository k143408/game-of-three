package com.exercise.got.factory;

import com.exercise.got.enums.PlayerType;
import com.exercise.got.strategy.MoveStrategy;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class MoveStrategyFactory {
    private final Map<PlayerType, MoveStrategy> strategyMap = new HashMap<>();
    private final MoveStrategy manualMoveStrategy;
    private final MoveStrategy randomMoveStrategy;

    @PostConstruct
    public void init() {
        strategyMap.put(PlayerType.AUTOMATIC, randomMoveStrategy);
        strategyMap.put(PlayerType.MANUAL, manualMoveStrategy);
        log.debug("MoveStrategyFactory initialized");
    }

    public MoveStrategy getStrategy(PlayerType playerType) {
        return strategyMap.get(playerType);
    }
}
