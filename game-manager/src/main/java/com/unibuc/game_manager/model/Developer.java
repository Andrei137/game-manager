package com.unibuc.game_manager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@Table(name = "developer")
public class Developer extends Provider {

    @OneToMany(mappedBy = "developer")
    @JsonIgnore
    private List<Game> developedGames;

    @OneToMany(mappedBy = "developer")
    @JsonIgnore
    private List<Contract> contracts;

}
