package com.unibuc.game_manager.mapper;

import com.unibuc.game_manager.dto.ContractDto;
import com.unibuc.game_manager.model.Contract;
import com.unibuc.game_manager.model.ContractId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public final class ContractMapper {

    public void updateEntityFromDto(ContractDto dto, Contract entity) {
        Optional.ofNullable(dto.getCutPercentage()).ifPresent(entity::setCutPercentage);
        Optional.ofNullable(dto.getExpiryDate()).ifPresent(entity::setExpiryDate);
    }

    public Contract toEntity(ContractDto dto) {
        var builder = Contract.builder()
                .id(new ContractId())
                .createdAt(LocalDate.now())
                .expiryDate(dto.getExpiryDate())
                .status(Contract.Status.PENDING);
        Optional.ofNullable(dto.getCutPercentage())
                .ifPresent(builder::cutPercentage);
        return builder.build();
    }

}
