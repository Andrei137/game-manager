package com.unibuc.game_manager.repository;

import com.unibuc.game_manager.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {

    List<Game> getGamesByDeveloperId(Integer id);

    List<Game> getGamesByPublisherId(Integer id);

}
