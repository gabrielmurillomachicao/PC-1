package com.ejemplo.pc1.model;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private route route;
    @Min(1)
    private Integer capacity;
    @Transient
    private SeatStatus status;
    @Column(nullable = false)
    private ZonedDateTime departureTime;
    @Min(0)
    @Column(nullable = false)
    private Integer availableSeats;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TripStatus tripStatus;
}
