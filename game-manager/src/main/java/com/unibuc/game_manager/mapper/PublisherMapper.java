package com.unibuc.game_manager.mapper;

import com.unibuc.game_manager.dto.PublisherDto;
import com.unibuc.game_manager.model.ProviderStatus;
import com.unibuc.game_manager.model.Publisher;
import com.unibuc.game_manager.service.JWTService;
import org.springframework.stereotype.Component;

@Component
public final class PublisherMapper extends ProviderMapper<Publisher, PublisherDto> {

    public Publisher toEntity(PublisherDto dto) {
        return Publisher.builder()
                .username(dto.getUsername())
                .password(JWTService.encryptPassword(dto.getPassword()))
                .email(dto.getEmail())
                .website(dto.getWebsite())
                .status(ProviderStatus.PENDING)
                .build();
    }

}
