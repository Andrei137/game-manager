package com.unibuc.game_manager.service;

import com.unibuc.game_manager.dto.GameCreateDto;
import com.unibuc.game_manager.dto.GameResponseDto;
import com.unibuc.game_manager.dto.GameUpdateDto;
import com.unibuc.game_manager.exception.ForbiddenException;
import com.unibuc.game_manager.exception.NotFoundException;
import com.unibuc.game_manager.exception.ValidationException;
import com.unibuc.game_manager.mapper.GameMapper;
import com.unibuc.game_manager.model.Developer;
import com.unibuc.game_manager.model.Game;
import com.unibuc.game_manager.model.Provider;
import com.unibuc.game_manager.repository.GameRepository;
import com.unibuc.game_manager.utils.EnumUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public final class GameService {

    private final GameRepository gameRepository;
    private final JWTService jwtService;
    private final GameMapper gameMapper;

    public Provider getCurrentProvider() {
        if (jwtService.getUser() instanceof Provider provider) return provider;
        return null;
    }

    private Provider getGameOwner(Game game) {
        Provider owner = game.getPublisher();
        if (owner != null) return owner;
        return game.getDeveloper();
    }

    public List<GameResponseDto> getAllGamesOfCurrentProvider(String status, String title) {
        Game.Status statusObj = EnumUtils.fromString(Game.Status.class, status);
        String normalizedTitle = (title == null) ? "" : title.toLowerCase().trim();

        Stream<Game> gamesStream;
        Provider provider = getCurrentProvider();
        assert provider != null;
        if (provider instanceof Developer) {
            gamesStream = gameRepository.getAllGamesByDeveloperId(provider.getId()).stream();
        } else {
            gamesStream = gameRepository.getAllGamesByPublisherId(provider.getId()).stream();
        }

        return gamesStream
                .filter(g -> statusObj == null || g.getStatus().equals(statusObj))
                .filter(g -> g.getTitle().toLowerCase().contains(normalizedTitle))
                .map(gameMapper::toResponseDto)
                .toList();
    }

    public GameResponseDto announceGame(GameCreateDto gameCreateDto) {
        Game game = gameMapper.toEntity(gameCreateDto);
        game.setDeveloper((Developer) getCurrentProvider());
        return gameMapper.toResponseDto(gameRepository.save(game));
    }

    public GameResponseDto updateGame(Integer gameId, GameUpdateDto gameUpdateDto) {
        Game game = gameRepository.findById(gameId).orElseThrow(
                () -> new NotFoundException(String.format("Game with id %d not found", gameId))
        );
        if (!getGameOwner(game).equals(getCurrentProvider())) {
            throw new ForbiddenException("Not enough permissions");
        }

        String status = gameUpdateDto.getStatus();
        if (status != null) {
            Game.Status newStatus = Game.Status.valueOf(status.toUpperCase());
            Game.Status currentStatus = game.getStatus();
            if (!Game.Status.isValidTransition(currentStatus, newStatus)) {
                throw new ValidationException(String.format(
                        "Cannot change status from %s to %s",
                        EnumUtils.toString(currentStatus),
                        EnumUtils.toString(newStatus)
                ));
            }
            game.setStatus(newStatus);
            if (newStatus.equals(Game.Status.PUBLISHED)) {
                game.setPublisher(getCurrentProvider());
            }
        }

        gameMapper.updateEntityFromDto(gameUpdateDto, game);
        return gameMapper.toResponseDto(gameRepository.save(game));
    }

}
