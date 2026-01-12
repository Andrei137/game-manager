package com.unibuc.game_manager.dto;

import com.unibuc.game_manager.utils.ValidationUtils;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class GameCreateDto {

    @NotBlank(message = "title is required and cannot be blank", groups = ValidationUtils.Create.class)
    private String title;

    @DecimalMin(value = "0.0", message = "Price must be greater or equal to 0")
    private Double price;

}
