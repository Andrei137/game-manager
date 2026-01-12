package com.unibuc.game_manager.service;

import com.unibuc.game_manager.dto.DeveloperDto;
import com.unibuc.game_manager.mapper.DeveloperMapper;
import com.unibuc.game_manager.model.Developer;
import com.unibuc.game_manager.repository.DeveloperRepository;
import com.unibuc.game_manager.repository.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public final class DeveloperService extends ProviderService<Developer, DeveloperDto> {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private DeveloperRepository developerRepository;

    @Autowired
    private DeveloperMapper developerMapper;

    @Override
    protected ProviderRepository<Developer> getRepository() {
        return developerRepository;
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
