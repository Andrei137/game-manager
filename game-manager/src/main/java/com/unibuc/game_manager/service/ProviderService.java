package com.unibuc.game_manager.service;

import com.unibuc.game_manager.dto.ProviderCreateDto;
import com.unibuc.game_manager.exception.NotFoundException;
import com.unibuc.game_manager.model.Provider;
import com.unibuc.game_manager.repository.ProviderRepository;

import java.util.List;

public abstract class ProviderService<P extends Provider, D extends ProviderCreateDto> extends UserService<P, D> {

    protected abstract ProviderRepository<P> getRepository();

    public final List<P> getProvidersByStatus(Provider.Status status) {
        return getRepository().findByStatus(status);
    }

    public final P getProviderByIdAndStatus(Integer id, Provider.Status status) {
        return getRepository()
                .findByIdAndStatus(id, status)
                .orElseThrow(() -> new NotFoundException(String.format("No %s found at id %d", getEntityName(), id)));
    }

}
