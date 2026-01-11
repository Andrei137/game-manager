package com.unibuc.game_manager.service;

import com.unibuc.game_manager.dto.ProviderDto;
import com.unibuc.game_manager.exception.NotFoundException;
import com.unibuc.game_manager.exception.ValidationException;
import com.unibuc.game_manager.model.Provider;
import com.unibuc.game_manager.model.ProviderStatus;
import com.unibuc.game_manager.repository.ProviderRepository;

import java.util.List;
import java.util.Map;

public abstract class ProviderService<P extends Provider, D extends ProviderDto> extends UserService<P, D> {

    protected abstract ProviderRepository<P> getRepository();
    public abstract Class<P> getProviderClass();

    private static final Map<String, ProviderStatus> actionToStatus = Map.of(
            "reject", ProviderStatus.REJECTED,
            "approve", ProviderStatus.APPROVED,
            "unban", ProviderStatus.APPROVED,
            "ban", ProviderStatus.BANNED
    );

    private boolean isValidTransition(ProviderStatus from, ProviderStatus to) {
        return switch (from) {
            case ProviderStatus.PENDING -> to == ProviderStatus.APPROVED || to == ProviderStatus.REJECTED;
            case ProviderStatus.BANNED -> to == ProviderStatus.APPROVED;
            case ProviderStatus.APPROVED -> to == ProviderStatus.BANNED;
            default -> false;
        };
    }

    public final List<P> getProvidersByStatus(ProviderStatus status) {
        return getRepository().findByStatus(status);
    }

    public final P getProviderByIdAndStatus(Integer id, ProviderStatus status) {
        return getRepository()
                .findByIdAndStatus(id, status)
                .orElseThrow(() -> new NotFoundException(String.format("No %s found at id %d", getEntityName(), id)));
    }

    public List<P> getAll(
            String status,
            String name
    ) {
        String normalizedName = (name == null) ? "" : name.toLowerCase().trim();
        return ((status != null && !status.isBlank())
                ? getRepository().findByStatus(ProviderStatus.fromString(status.trim()))
                : getRepository().findAll()
        ).stream().filter(provider ->
                provider.getUsername().toLowerCase().contains(normalizedName)
        ).toList();

    }

    public P changeStatus(
            Integer id,
            String action
    ) {
        String normalizedAction = action.toLowerCase().trim();
        if (!actionToStatus.containsKey(normalizedAction)) {
            throw new ValidationException(String.format("Invalid action %s", action));
        }

        P provider = getRepository().findById(id).orElseThrow(
                () -> new NotFoundException(String.format("%s with id %d not found", getEntityName(), id))
        );
        ProviderStatus currentStatus = provider.getStatus();
        ProviderStatus newStatus = actionToStatus.get(normalizedAction);

        if (!isValidTransition(currentStatus, newStatus)) {
            throw new ValidationException(String.format(
                    "Cannot change status from %s to %s",
                    ProviderStatus.toString(currentStatus),
                    ProviderStatus.toString(newStatus)
            ));
        }

        provider.setStatus(newStatus);
        return getRepository().save(provider);
    }

}
