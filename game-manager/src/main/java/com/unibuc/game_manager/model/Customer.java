package com.unibuc.game_manager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@Table(name = "customer")
public final class Customer extends User {

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String firstName;

    @Column(unique = true)
    private String phoneNumber;

    @ManyToMany
    @JoinTable(
            name = "library",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "game_id")
    )
    @JsonIgnore
    private List<Game> ownedGames;

    @ManyToMany
    @JoinTable(
            name = "wishlist",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "game_id")
    )
    @JsonIgnore
    private List<Game> wishlistedGames;

}
