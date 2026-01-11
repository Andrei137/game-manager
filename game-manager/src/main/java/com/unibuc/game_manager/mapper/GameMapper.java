package com.unibuc.game_manager.mapper;

import com.unibuc.game_manager.dto.GameCreateDto;
import com.unibuc.game_manager.dto.GameResponseDto;
import com.unibuc.game_manager.dto.GameUpdateDto;
import com.unibuc.game_manager.model.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public final class GameMapper {

    public void updateEntityFromDto(GameUpdateDto dto, Game entity) {
        Optional.ofNullable(dto.getDiscountPercentage()).ifPresent(entity::setDiscountPercentage);
    }

    public Game toEntity(GameCreateDto dto) {
        return Game.builder()
                .title(dto.getTitle())
                .price(Optional.ofNullable(dto.getPrice()).orElse(0.0))
                .releaseDate(LocalDate.now())
                .status(Game.Status.ANNOUNCED)
                .build();
    }

    public GameResponseDto toResponseDto(Game entity) {
        return GameResponseDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .price(entity.getPrice() - entity.getPrice() * entity.getDiscountPercentage() / 100)
                .releaseDate(entity.getReleaseDate())
                .discountPercentage(entity.getDiscountPercentage())
                .initialPrice(entity.getPrice())
                .status(entity.getStatus())
                .build();
    }

}