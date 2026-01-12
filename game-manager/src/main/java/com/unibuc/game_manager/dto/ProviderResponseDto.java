package com.unibuc.game_manager.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.unibuc.game_manager.model.Provider;
import com.unibuc.game_manager.utils.ViewUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public final class ProviderResponseDto {

    @JsonView(ViewUtils.Public.class)
    private Integer id;

    @JsonView(ViewUtils.Public.class)
    private String username;

    @JsonView(ViewUtils.Public.class)
    private String email;

    @JsonView(ViewUtils.Public.class)
    private String website;

    @JsonView(ViewUtils.Admin.class)
    private String type;

    @JsonView(ViewUtils.Admin.class)
    private Provider.Status status;

}
