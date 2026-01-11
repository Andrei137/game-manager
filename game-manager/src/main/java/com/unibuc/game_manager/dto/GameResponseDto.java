package com.unibuc.game_manager.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.unibuc.game_manager.model.Game;
import com.unibuc.game_manager.utils.ViewUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class GameResponseDto {

    @JsonView(ViewUtils.Public.class)
    private Integer id;

    @JsonView(ViewUtils.Public.class)
    private String title;

    @JsonView(ViewUtils.Public.class)
    private Double price;

    @JsonView(ViewUtils.Public.class)
    private LocalDate releaseDate;

    @JsonView(ViewUtils.Provider.class)
    private Integer discountPercentage;

    @JsonView(ViewUtils.Provider.class)
    private Double initialPrice;

    @JsonView(ViewUtils.Provider.class)
    private Game.Status status;

}
