package com.unibuc.game_manager.mapper;

import com.unibuc.game_manager.dto.UserDto;
import com.unibuc.game_manager.model.User;
import com.unibuc.game_manager.service.JWTService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public abstract class UserMapper<U extends User, D extends UserDto> {

    public void updateEntityFromDto(D dto, U entity) {
        Optional.ofNullable(dto.getUsername()).ifPresent(entity::setUsername);
        Optional.ofNullable(dto.getPassword()).ifPresent(entity::setPassword);
        Optional.ofNullable(dto.getPassword())
                .ifPresent(raw -> entity.setPassword(JWTService.encryptPassword(raw)));
        Optional.ofNullable(dto.getEmail()).ifPresent(entity::setEmail);
    }

}