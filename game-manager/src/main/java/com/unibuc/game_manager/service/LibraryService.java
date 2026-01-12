package com.unibuc.game_manager.service;

import com.unibuc.game_manager.exception.NotFoundException;
import com.unibuc.game_manager.model.Customer;
import com.unibuc.game_manager.model.Game;
import com.unibuc.game_manager.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LibraryService {

    private final JWTService jwtService;

    public List<Game> getOwnedGames() {
        User currentUser = jwtService.getCurrentUser();
        assert currentUser instanceof Customer;

        return ((Customer) currentUser).getOwnedGames();
    }

    public Game getOwnedGameById(Integer gameId) {
        return getOwnedGames()
                .stream()
                .filter(game -> game.getId().equals(gameId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Game at id %d not owned or doesn't exist", gameId)));
    }

}
