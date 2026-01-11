package com.unibuc.game_manager.mapper;

import com.unibuc.game_manager.dto.ProviderCreateDto;
import com.unibuc.game_manager.model.Provider;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public abstract class ProviderMapper<U extends Provider, D extends ProviderCreateDto> extends UserMapper<U, D> {

    @Override
    public void updateEntityFromDto(D dto, U entity) {
        super.updateEntityFromDto(dto, entity);
        Optional.ofNullable(dto.getWebsite()).ifPresent(entity::setWebsite);
    }

}