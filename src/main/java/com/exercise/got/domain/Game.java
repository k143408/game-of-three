package com.exercise.got.domain;

import com.exercise.got.enums.GameState;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.EnumType.STRING;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Player player1;

    @ManyToOne
    private Player player2;

    private Integer startingNumber;
    private Integer currentNumber;

    @ManyToOne
    private Player winner;

    @ManyToOne
    private Player currentPlayer;

    @Enumerated(STRING)
    private GameState gameState;
}
