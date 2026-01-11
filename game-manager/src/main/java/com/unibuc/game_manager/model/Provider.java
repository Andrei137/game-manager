package com.unibuc.game_manager.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.unibuc.game_manager.utils.ViewUtils;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Arrays;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@Table(name = "provider")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Provider extends User {

    public enum Status {
        PENDING,
        APPROVED,
        REJECTED,
        BANNED;

        public static boolean isValidTransition(Status from, Status to) {
            return switch (from) {
                case PENDING -> to == APPROVED || to == REJECTED;
                case BANNED -> to == APPROVED;
                case Status.APPROVED -> to == BANNED;
                default -> false;
            };
        }
    }

    @Column(unique = true)
    @JsonView(ViewUtils.Public.class)
    private String website;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    @JsonView(ViewUtils.Admin.class)
    private Status status = Status.PENDING;

}
