package com.unibuc.game_manager.service;

import com.unibuc.game_manager.dto.PublisherDto;
import com.unibuc.game_manager.mapper.PublisherMapper;
import com.unibuc.game_manager.model.Publisher;
import com.unibuc.game_manager.repository.ProviderRepository;
import com.unibuc.game_manager.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class PublisherService extends ProviderService<Publisher, PublisherDto> {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private PublisherMapper publisherMapper;

    @Override
    protected ProviderRepository<Publisher> getRepository() {
        return publisherRepository;
    }

    @Override
    protected String getEntityName() {
        return "publisher";
    }

    @Override
    protected PublisherMapper getMapper() {
        return publisherMapper;
    }

}
