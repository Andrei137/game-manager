package com.unibuc.game_manager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.unibuc.game_manager.utils.ViewUtils;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@Entity
@Table(name = "game")
@Inheritance(strategy = InheritanceType.JOINED)
public class Game {

    public enum Status {
        ANNOUNCED,
        DEVELOPED,
        PUBLISHED,
        DELISTED;

        public static boolean isValidTransition(Status from, Status to) {
            return switch (from) {
                case ANNOUNCED -> to == DEVELOPED;
                case DEVELOPED -> to == PUBLISHED || to == DELISTED;
                case PUBLISHED -> to == DELISTED;
                case DELISTED -> to == PUBLISHED;
            };
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(ViewUtils.Public.class)
    private Integer id;

    @Column(unique = true, nullable = false)
    @JsonView(ViewUtils.Public.class)
    private String title;

    @Column(nullable = false)
    @JsonView(ViewUtils.Public.class)
    private Double price;

    @Column(nullable = false)
    @JsonView(ViewUtils.Public.class)
    @Builder.Default
    private Integer discountPercentage = 0;

    @Column(nullable = false)
    @JsonView(ViewUtils.Public.class)
    @Builder.Default
    private LocalDate releaseDate = LocalDate.now();

    @Column(nullable = false)
    @JsonView(ViewUtils.Provider.class)
    @Builder.Default
    private Status status = Status.ANNOUNCED;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "developer_id", nullable = false)
    @JsonIgnore
    private Developer developer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    @JsonIgnore
    private Provider publisher;

}
