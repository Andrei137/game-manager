package com.unibuc.game_manager.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
public final class DeveloperDto extends ProviderCreateDto { }
