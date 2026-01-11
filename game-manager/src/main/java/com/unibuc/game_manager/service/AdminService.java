package com.unibuc.game_manager.service;

import com.unibuc.game_manager.model.Developer;
import com.unibuc.game_manager.model.Publisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class AdminService {

    private final DeveloperService developerService;
    private final PublisherService publisherService;

    public List<Developer> getDevelopers(String status, String name) {
        return developerService.getAll(status, name.toLowerCase().trim());
    }

    public List<Publisher> getPublishers(String status, String name) {
        return publisherService.getAll(status, name.toLowerCase().trim());
    }

    public Developer changeDeveloperStatus(Integer id, String action) {
        return developerService.changeStatus(id, action.toLowerCase().trim());
    }

    public Publisher changePublisherStatus(Integer id, String action) {
        return publisherService.changeStatus(id, action.toLowerCase().trim());
    }

}
