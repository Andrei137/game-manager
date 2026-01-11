package com.unibuc.game_manager.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class ProviderCreateDto extends UserDto {

    @Pattern(
            regexp = "^(https?://)?([\\w-]+\\.)+[\\w-]{2,}(/\\S*)?$",
            message = "Website must be a valid URL"
    )
    private String website;

}
