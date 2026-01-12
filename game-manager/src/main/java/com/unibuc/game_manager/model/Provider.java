package com.unibuc.game_manager.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.unibuc.game_manager.utils.EnumUtils;
import com.unibuc.game_manager.utils.ViewUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@Table(name = "provider")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Provider extends User implements EnumUtils.HasStatus<Provider.Status> {

    public enum Status implements EnumUtils.TransitionAware<Status> {
        PENDING,
        APPROVED,
        REJECTED,
        BANNED;

        @Override
        public boolean canTransitionFrom(Status from) {
            return switch (from) {
                case PENDING -> this == APPROVED || this == REJECTED;
                case BANNED -> this == APPROVED;
                case Status.APPROVED -> this == BANNED;
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
