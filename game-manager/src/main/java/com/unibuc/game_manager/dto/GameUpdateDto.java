package com.unibuc.game_manager.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameUpdateDto {

    @Min(value = 0, message = "Discount percentage must be at least 0")
    @Max(value = 100, message = "Discount percentage cannot be more than 100")
    private Integer discountPercentage;

    @Pattern(
            regexp = "DEVELOPED|PUBLISHED|DELISTED",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "status must be one of DEVELOPED, PUBLISHED, DELISTED"
    )
    private String status;

}
