package com.unibuc.game_manager.service;

import com.unibuc.game_manager.exception.NotFoundException;
import com.unibuc.game_manager.model.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class LibraryService {

    private final JwtService jwtService;

    public List<Game> getOwnedGames() {
        return jwtService.getCurrentCustomer().getOwnedGames();
    }

    public Game getOwnedGameById(Integer gameId) {
        return getOwnedGames()
                .stream()
                .filter(game -> game.getId().equals(gameId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Game at id %d not owned or doesn't exist", gameId)));
    }

}
