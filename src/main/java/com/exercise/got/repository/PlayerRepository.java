package com.exercise.got.repository;

import com.exercise.got.domain.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {
}
