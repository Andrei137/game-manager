package com.unibuc.game_manager.mapper;

import com.unibuc.game_manager.dto.CustomerDto;
import com.unibuc.game_manager.model.Customer;
import com.unibuc.game_manager.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public final class CustomerMapper extends UserMapper<Customer, CustomerDto> {

    @Override
    public void updateEntityFromDto(CustomerDto dto, Customer entity) {
        super.updateEntityFromDto(dto, entity);
        Optional.ofNullable(dto.getFirstName()).ifPresent(entity::setFirstName);
        Optional.ofNullable(dto.getLastName()).ifPresent(entity::setLastName);
        Optional.ofNullable(dto.getPhoneNumber()).ifPresent(entity::setPhoneNumber);
    }

    public Customer toEntity(CustomerDto dto) {
        return Customer.builder()
                .username(dto.getUsername())
                .password(JwtService.encryptPassword(dto.getPassword()))
                .email(dto.getEmail())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phoneNumber(dto.getPhoneNumber())
                .build();
    }

}