package com.unibuc.game_manager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@Table(name = "developer")
public final class Developer extends Provider {

    @OneToMany(mappedBy = "developer", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Game> developedGames;

}
