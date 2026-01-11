package com.unibuc.game_manager.mapper;

import com.unibuc.game_manager.dto.DeveloperDto;
import com.unibuc.game_manager.model.Developer;
import com.unibuc.game_manager.model.Provider;
import com.unibuc.game_manager.service.JWTService;
import org.springframework.stereotype.Component;

@Component
public final class DeveloperMapper extends ProviderMapper<Developer, DeveloperDto> {

    public Developer toEntity(DeveloperDto dto) {
        return Developer.builder()
                .username(dto.getUsername())
                .password(JWTService.encryptPassword(dto.getPassword()))
                .email(dto.getEmail())
                .website(dto.getWebsite())
                .status(Provider.Status.PENDING)
                .build();
    }

}
