package com.ejemplo.pc1.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"trip_id", "passenger_id"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class seatRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private trip trip;
    @ManyToOne
    private user passenger;
    private LocalTime requesAt;
    @Transient
    private Routestatus status;
    private ZonedDateTime requestedAt;
    @Enumerated(EnumType.STRING)
    private SeatStatus seatStatus;
    @PrePersist
        void prepersist(){
            if (requesAt==null) requesAt= LocalTime.now();
            if (requestedAt == null) requestedAt = ZonedDateTime.now();
            if (seatStatus == null) seatStatus = SeatStatus.CONFIRMED;
        }
}
