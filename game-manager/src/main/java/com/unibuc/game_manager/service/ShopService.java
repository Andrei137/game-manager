package com.unibuc.game_manager.service;

import com.unibuc.game_manager.dto.GameResponseDto;
import com.unibuc.game_manager.exception.NotFoundException;
import com.unibuc.game_manager.exception.ValidationException;
import com.unibuc.game_manager.mapper.GameMapper;
import com.unibuc.game_manager.model.Customer;
import com.unibuc.game_manager.model.Game;
import com.unibuc.game_manager.repository.CustomerRepository;
import com.unibuc.game_manager.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class ShopService {

    private final CustomerRepository customerRepository;
    private final GameRepository gameRepository;
    private final GameService gameService;
    private final JwtService jwtService;
    private final GameMapper gameMapper;

    public List<GameResponseDto> getUnownedGames() {
        Customer customer = jwtService.getCurrentCustomer();
        assert customer != null;
        return gameRepository
                .findAll()
                .stream()
                .filter(g -> g.getStatus().equals(Game.Status.PUBLISHED))
                .filter(g -> !customer.getOwnedGames().contains(g))
                .map(gameMapper::toResponseDto)
                .toList();
    }

    public GameResponseDto getUnownedGameById(Integer gameId) {
        return getUnownedGames()
                .stream()
                .filter(g -> g.getId().equals(gameId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Game at id %d owned or doesn't exist", gameId)));
    }

    public void buyGame(Integer gameId) {
        Game game = gameService.getGame(gameId);
        Customer customer = jwtService.getCurrentCustomer();

        if (customer.getOwnedGames().contains(game)) {
            throw new ValidationException("Game already owned");
        }

        customer.getOwnedGames().add(game);
        customerRepository.save(customer);
    }

}
