package com.unibuc.game_manager.model;

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

    private Integer developerId;
    private Integer publisherId;
    private Integer gameId;

}
