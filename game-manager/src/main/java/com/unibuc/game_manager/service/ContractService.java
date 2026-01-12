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
import com.unibuc.game_manager.repository.DeveloperRepository;
import com.unibuc.game_manager.repository.GameRepository;
import com.unibuc.game_manager.repository.PublisherRepository;
import com.unibuc.game_manager.utils.EnumUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class ContractService {

    private final ContractRepository contractRepository;
    private final GameRepository gameRepository;
    private final DeveloperRepository developerRepository;
    private final PublisherRepository publisherRepository;
    private final JWTService jwtService;
    private final ContractMapper contractMapper;

    public Provider getCurrentProvider() {
        if (jwtService.getUser() instanceof Provider provider) return provider;
        return null;
    }

    public List<Contract> getAllContracts() {
        Provider provider = getCurrentProvider();
        assert provider != null;
        if (provider instanceof Developer) {
            return contractRepository.getContractsByDeveloperId(provider.getId());
        } else {
            return contractRepository.getContractsByPublisherId(provider.getId());
        }
    }

    public Contract issueContract(ContractDto contractDto) {
        Contract contract = contractMapper.toEntity(contractDto);
        contract.setPublisher((Publisher) jwtService.getUser());

        Integer developerId = contractDto.getPartnerId();
        Developer developer = developerRepository
                .findById(developerId)
                .orElseThrow(() -> new ValidationException(String.format("developer not found at id %s", developerId)));
        contract.setDeveloper(developer);

        Integer gameId = contractDto.getGameId();
        Game game = gameRepository
                .findById(gameId)
                .orElseThrow(() -> new ValidationException(String.format("game not found at id %s", gameId)));
        contract.setGame(game);

        List<Contract> otherContracts = contractRepository.getContractsByDeveloperIdAndGameId(developerId, gameId);
        if (otherContracts
                .stream()
                .anyMatch(c -> c.getStatus() == Contract.Status.ACCEPTED)
        ) {
            throw new ValidationException(
                    String.format("Cannot create contract: developer %s already has an accepted contract for game %s",
                            developerId, gameId)
            );
        }

        return contractRepository.save(contract);
    }

    public Contract updateContract(ContractDto contractDto) {
        Provider currentUser = getCurrentProvider();
        Integer partnerId = contractDto.getPartnerId();
        Integer gameId = contractDto.getGameId();

        if (currentUser instanceof Developer) {
            Publisher publisher = publisherRepository
                    .findById(partnerId)
                    .orElseThrow(() -> new ValidationException(String.format("publisher not found at id %s", partnerId)));
            Contract contract = contractRepository.getContractByDeveloperIdAndPublisherIdAndGameId(currentUser.getId(), publisher.getId(), gameId);
            if (contract == null) {
                throw new ValidationException(String.format("contract not found with publisher %s and game %s", publisher.getId(), gameId));
            }
            Contract.Status currentStatus = contract.getStatus();
            if (contractDto.getStatus() == null || !currentStatus.equals(Contract.Status.PENDING)) {
                throw new ForbiddenException("Contract no longer pending, no changes allowed");
            }
            Contract.Status newStatus = Contract.Status.valueOf(contractDto.getStatus().toUpperCase());
            if (!Contract.Status.isValidTransition(currentStatus, newStatus)) {
                throw new ValidationException(String.format(
                        "Cannot change status from %s to %s",
                        EnumUtils.toString(currentStatus),
                        EnumUtils.toString(newStatus)
                ));
            }
            List<Contract> otherContracts = contractRepository.getContractsByDeveloperIdAndGameId(currentUser.getId(), gameId);
            if (otherContracts
                    .stream()
                    .anyMatch(c -> c.getStatus() == Contract.Status.ACCEPTED)
            ) {
                throw new ValidationException(
                        String.format("Cannot accept contract: already accepted contract for game %s",
                                currentUser.getId(), gameId)
                );
            }
            contract.setStatus(newStatus);
            return contractRepository.save(contract);
        } else {
            Developer developer = developerRepository
                    .findById(partnerId)
                    .orElseThrow(() -> new ValidationException(String.format("developer not found at id %s", partnerId)));
            Contract contract = contractRepository.getContractByDeveloperIdAndPublisherIdAndGameId(developer.getId(), currentUser.getId(), gameId);
            if (contract == null) {
                throw new ValidationException(String.format("contract not found with developer %s and game %s", developer.getId(), gameId));
            }
            if (contract.getStatus() != Contract.Status.PENDING) {
                throw new ForbiddenException("Contract no longer pending, no changes allowed");
            }
            contractMapper.updateEntityFromDto(contractDto, contract);
            return contractRepository.save(contract);
        }
    }

    public void deleteContract(ContractDto contractDto) {
        Provider currentUser = getCurrentProvider();
        Integer developerId = contractDto.getPartnerId();
        Integer gameId = contractDto.getGameId();

        Contract contract = contractRepository.getContractByDeveloperIdAndPublisherIdAndGameId(developerId, currentUser.getId(), gameId);
        if (contract == null) {
            throw new NotFoundException(String.format("contract not found with developer %s and game %s", developerId, gameId));
        }
        if (contract.getStatus() == Contract.Status.ACCEPTED) {
            throw new ForbiddenException("Cannot delete accepted contract");
        }
        contractRepository.delete(contract);
    }

}
