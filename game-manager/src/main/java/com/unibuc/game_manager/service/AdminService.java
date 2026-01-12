package com.unibuc.game_manager.service;

import com.unibuc.game_manager.dto.ProviderResponseDto;
import com.unibuc.game_manager.exception.NotFoundException;
import com.unibuc.game_manager.exception.ValidationException;
import com.unibuc.game_manager.mapper.DeveloperMapper;
import com.unibuc.game_manager.mapper.PublisherMapper;
import com.unibuc.game_manager.model.Provider;
import com.unibuc.game_manager.repository.DeveloperRepository;
import com.unibuc.game_manager.repository.ProviderRepository;
import com.unibuc.game_manager.repository.PublisherRepository;
import com.unibuc.game_manager.utils.EnumUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public final class AdminService {

    private final DeveloperRepository developerRepository;
    private final PublisherRepository publisherRepository;
    private final DeveloperMapper developerMapper;
    private final PublisherMapper publisherMapper;

    private <P extends Provider> P _changeProviderStatus(
            ProviderRepository<P> repository,
            Integer id,
            String status
    ) {
        P entity = repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Entity with id " + id + " not found"));

        Provider.Status newStatus = Provider.Status.valueOf(status.toUpperCase());
        Provider.Status currentStatus = entity.getStatus();
        if (!Provider.Status.isValidTransition(currentStatus, newStatus)) {
            throw new ValidationException(String.format(
                    "Cannot change status from %s to %s",
                    EnumUtils.toString(currentStatus),
                    EnumUtils.toString(newStatus)
            ));
        }
        entity.setStatus(newStatus);

        return repository.save(entity);
    }

    private <P extends Provider> List<P> getProviders(
            ProviderRepository<P> repository,
            String status,
            String name
    ) {
        Provider.Status statusObj = EnumUtils.fromString(Provider.Status.class, status);
        String normalizedName = (name == null) ? "" : name.toLowerCase().trim();

        return repository
                .findAll()
                .stream()
                .filter(p -> statusObj == null || p.getStatus().equals(statusObj))
                .filter(p -> normalizedName.isEmpty() || p.getUsername().toLowerCase().contains(normalizedName))
                .toList();
    }

    public Provider changeProviderStatus(Integer id, String status) {
        return developerRepository.existsById(id)
                ? _changeProviderStatus(developerRepository, id, status)
                : _changeProviderStatus(publisherRepository, id, status);
    }

    public List<ProviderResponseDto> getProviders(String status, String name) {
        List<ProviderResponseDto> developers = getProviders(developerRepository, status, name)
                .stream()
                .map(dev -> developerMapper.toProviderResponseDto(dev, "developer"))
                .toList();

        List<ProviderResponseDto> publishers = getProviders(publisherRepository, status, name)
                .stream()
                .map(pub -> publisherMapper.toProviderResponseDto(pub, "publisher"))
                .toList();

        return Stream
                .concat(developers.stream(), publishers.stream())
                .toList();
    }

}
