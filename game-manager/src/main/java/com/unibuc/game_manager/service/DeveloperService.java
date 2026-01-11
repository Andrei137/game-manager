package com.unibuc.game_manager.service;

import com.unibuc.game_manager.dto.DeveloperDto;
import com.unibuc.game_manager.mapper.DeveloperMapper;
import com.unibuc.game_manager.model.Developer;
import com.unibuc.game_manager.repository.DeveloperRepository;
import com.unibuc.game_manager.repository.GameRepository;
import com.unibuc.game_manager.repository.ProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public final class DeveloperService extends ProviderService<Developer, DeveloperDto> {

    private final DeveloperRepository developerRepository;
    private final GameRepository gameRepository;
    private final DeveloperMapper developerMapper;

    @Override
    protected ProviderRepository<Developer> getRepository() {
        return developerRepository;
    }

    @Override
    public Class<Developer> getProviderClass() {
        return Developer.class;
    }

    @Override
    protected String getEntityName() {
        return "developer";
    }

    @Override
    protected DeveloperMapper getMapper() {
        return developerMapper;
    }

}
