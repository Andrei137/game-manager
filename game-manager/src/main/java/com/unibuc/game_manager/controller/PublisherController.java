package com.unibuc.game_manager.controller;

import com.unibuc.game_manager.dto.PublisherDto;
import com.unibuc.game_manager.model.Publisher;
import com.unibuc.game_manager.service.ProviderService;
import com.unibuc.game_manager.service.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("publishers")
@RequiredArgsConstructor
public class PublisherController extends ProviderController<Publisher, PublisherDto> {

    private final PublisherService publisherService;

    @Override
    public ProviderService<Publisher, PublisherDto> getService() {
        return publisherService;
    }
}
