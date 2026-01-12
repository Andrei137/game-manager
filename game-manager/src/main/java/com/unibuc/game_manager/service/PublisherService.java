package com.unibuc.game_manager.service;

import com.unibuc.game_manager.dto.PublisherDto;
import com.unibuc.game_manager.mapper.PublisherMapper;
import com.unibuc.game_manager.model.Game;
import com.unibuc.game_manager.model.Publisher;
import com.unibuc.game_manager.repository.GameRepository;
import com.unibuc.game_manager.repository.ProviderRepository;
import com.unibuc.game_manager.repository.PublisherRepository;
import com.unibuc.game_manager.utils.EnumUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class PublisherService extends ProviderService<Publisher, PublisherDto> {

    private final PublisherRepository publisherRepository;
    private final GameRepository gameRepository;
    private final PublisherMapper publisherMapper;

    @Override
    protected ProviderRepository<Publisher> getRepository() {
        return publisherRepository;
    }

    @Override
    public Class<Publisher> getProviderClass() {
        return Publisher.class;
    }

    @Override
    protected String getEntityName() {
        return "publisher";
    }

    @Override
    protected PublisherMapper getMapper() {
        return publisherMapper;
    }

    public List<Game> getAllGamesByPublisherId(Integer id, String status, String title) {
        Game.Status statusObj = EnumUtils.fromString(Game.Status.class, status);
        String normalizedTitle = (title == null) ? "" : title.toLowerCase().trim();

        return gameRepository
                .getGamesByPublisherId(id)
                .stream()
                .filter(g -> statusObj == null || g.getStatus().equals(statusObj))
                .filter(g -> g.getTitle().toLowerCase().contains(normalizedTitle))
                .toList();
    }

    public List<Game> getAllGamesByCurrentPublisher(String status, String title) {
        return getAllGamesByPublisherId(getCurrentUser().getId(), status, title);
    }

}
