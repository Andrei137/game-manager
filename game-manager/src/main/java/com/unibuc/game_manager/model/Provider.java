package com.unibuc.game_manager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.unibuc.game_manager.utils.ViewUtils;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@Table(name = "provider")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Provider extends User {

    @Column(unique = true)
    @JsonView(ViewUtils.Public.class)
    private String website;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    @JsonView(ViewUtils.Admin.class)
    private ProviderStatus status = ProviderStatus.PENDING;

}
