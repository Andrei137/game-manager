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
@Table(name = "publisher")
public final class Publisher extends Provider {

    @OneToMany(mappedBy = "publisher")
    @JsonIgnore
    private List<Game> publishedGames;

}
