package com.unibuc.game_manager.service;

import com.unibuc.game_manager.dto.ContractDto;
import com.unibuc.game_manager.exception.ForbiddenException;
import com.unibuc.game_manager.exception.NotFoundException;
import com.unibuc.game_manager.exception.ValidationException;
import com.unibuc.game_manager.mapper.ContractMapper;
import com.unibuc.game_manager.model.Contract;
import com.unibuc.game_manager.model.Developer;
import com.unibuc.game_manager.model.Game;
import com.unibuc.game_manager.model.Provider;
import com.unibuc.game_manager.model.Publisher;
import com.unibuc.game_manager.repository.ContractRepository;
import com.unibuc.game_manager.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class ContractService {

    private final ContractRepository contractRepository;
    private final GameRepository gameRepository;
    private final JWTService jwtService;
    private final ContractMapper contractMapper;

    public List<Contract> getAllContracts() {
        Provider currentProvider = jwtService.getCurrentProvider();
        assert currentProvider != null;

        if (currentProvider instanceof Developer) {
            List<Game> developedGames = gameRepository.getGamesByDeveloperId(currentProvider.getId());
            return developedGames.stream()
                    .flatMap(game -> contractRepository
                            .getContractsByGameId(game.getId())
                            .stream())
                    .toList();
        } else {
            return contractRepository.getContractsByPublisherId(currentProvider.getId());
        }
    }

    public Contract issueContract(ContractDto contractDto, Integer gameId) {
        Provider currentProvider = jwtService.getCurrentProvider();
        assert currentProvider instanceof Publisher;

        Contract contract = contractMapper.toEntity(contractDto);
        contract.setPublisher((Publisher) currentProvider);

        Game game = gameRepository
                .findById(gameId)
                .orElseThrow(() -> new ValidationException(String.format("Game not found at id %s", gameId)));
        if (game.getStatus() == Game.Status.PUBLISHED || game.getStatus() == Game.Status.DELISTED) {
            throw new ValidationException(String.format("Game at id %s already published", gameId));
        }
        contract.setGame(game);

        List<Contract> otherContracts = contractRepository.getContractsByGameId(gameId);
        if (otherContracts
                .stream()
                .anyMatch(c -> c.getStatus() == Contract.Status.ACCEPTED)
        ) {
            throw new ValidationException(
                    String.format("Game at id %s already under contract", gameId)
            );
        }

        return contractRepository.save(contract);
    }

    public Contract updateContract(ContractDto contractDto, Integer gameId) {
        Provider currentProvider = jwtService.getCurrentProvider();
        assert currentProvider instanceof Publisher;

        Contract contract = contractRepository.getContractByPublisherIdAndGameId(currentProvider.getId(), gameId);
        if (contract == null) {
            throw new ValidationException(String.format("Contract not found with game %s", gameId));
        }
        if (contract.getStatus() != Contract.Status.PENDING) {
            throw new ForbiddenException("Contract no longer pending, no changes allowed");
        }
        contractMapper.updateEntityFromDto(contractDto, contract);
        return contractRepository.save(contract);
    }

    public void deleteContract(Integer gameId) {
        Provider currentProvider = jwtService.getCurrentProvider();
        assert currentProvider instanceof Publisher;

        Contract contract = contractRepository.getContractByPublisherIdAndGameId(currentProvider.getId(), gameId);
        if (contract == null) {
            throw new NotFoundException(String.format("contract not found with game %s", gameId));
        }
        if (contract.getStatus() == Contract.Status.ACCEPTED) {
            throw new ForbiddenException("Cannot delete accepted contract");
        }
        contractRepository.delete(contract);
    }

}
