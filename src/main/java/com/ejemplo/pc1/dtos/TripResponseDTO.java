package com.ejemplo.pc1.dtos;

import com.ejemplo.pc1.model.TripStatus;

import static com.ejemplo.pc1.model.TripStatus.SCHEDULED;

public class TripResponseDTO {
    private Long id;
    private String driverUsername;
    private String origin;
    private String destination;
    private Integer availableSeats;
    private TripStatus status= SCHEDULED;
}
