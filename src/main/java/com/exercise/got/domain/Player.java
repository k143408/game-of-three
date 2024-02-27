package com.exercise.got.domain;

import com.exercise.got.enums.PlayerType;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.EnumType.STRING;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @Builder
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private boolean available;

    @Enumerated(STRING)
    @Builder.Default private PlayerType type = PlayerType.AUTOMATIC;

}
