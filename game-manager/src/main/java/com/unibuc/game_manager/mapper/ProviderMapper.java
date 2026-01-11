package com.unibuc.game_manager.mapper;

import com.unibuc.game_manager.dto.ProviderDto;
import com.unibuc.game_manager.model.Provider;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public abstract class ProviderMapper<U extends Provider, D extends ProviderDto> extends UserMapper<U, D> {

    @Override
    public void updateEntityFromDto(D dto, U entity) {
        super.updateEntityFromDto(dto, entity);
        Optional.ofNullable(dto.getWebsite()).ifPresent(entity::setWebsite);
    }

}