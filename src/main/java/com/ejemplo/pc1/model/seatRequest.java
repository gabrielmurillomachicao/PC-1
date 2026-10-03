package com.ejemplo.pc1.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
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
    private Routestatus status;
    @PrePersist
        void prepersist(){
            if (requesAt==null) requesAt= LocalTime.now();
        }
}
