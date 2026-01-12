package com.unibuc.game_manager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@Entity
@Table(name = "contract")
public class Contract {

    public enum Status {
        PENDING,
        ACCEPTED,
        REJECTED;

        public static boolean isValidTransition(Status from, Status to) {
            return switch (from) {
                case PENDING -> to == ACCEPTED || to == REJECTED;
                case REJECTED -> to == PENDING;
                default -> false;
            };
        }
    }

    @EmbeddedId
    private ContractId id;

    @ManyToOne
    @MapsId("developerId")
    @JoinColumn(name = "developer_id", nullable = false)
    @JsonIgnore
    private Developer developer;

    @ManyToOne
    @MapsId("publisherId")
    @JoinColumn(name = "publisher_id", nullable = false)
    @JsonIgnore
    private Publisher publisher;

    @ManyToOne
    @MapsId("gameId")
    @JoinColumn(name = "game_id", nullable = false)
    @JsonIgnore
    private Game game;

    @Column(nullable = false)
    @Builder.Default
    private LocalDate createdAt = LocalDate.now();

    private LocalDate expiryDate;

    @Column(nullable = false)
    @Builder.Default
    private Integer cutPercentage = 30;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Status status = Status.PENDING;

}
