package com.unibuc.game_manager.controller;

import com.unibuc.game_manager.dto.DeveloperDto;
import com.unibuc.game_manager.model.Developer;
import com.unibuc.game_manager.service.DeveloperService;
import com.unibuc.game_manager.service.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("developers")
@RequiredArgsConstructor
public class DeveloperController extends ProviderController<Developer, DeveloperDto> {

    private final DeveloperService developerService;

    @Override
    public ProviderService<Developer, DeveloperDto> getService() {
        return developerService;
    }

}
