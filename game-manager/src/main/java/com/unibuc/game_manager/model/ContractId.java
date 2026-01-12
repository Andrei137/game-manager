package com.unibuc.game_manager.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.unibuc.game_manager.utils.ViewUtils;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractId implements Serializable {

    @JsonView(ViewUtils.Provider.class)
    private Integer publisherId;

    @JsonView(ViewUtils.Provider.class)
    private Integer gameId;

}
