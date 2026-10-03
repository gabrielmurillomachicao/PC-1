package com.ejemplo.pc1.dtos;

import com.ejemplo.pc1.model.SeatStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatRequestResponseDTO {
    private Long id;
    private Long tripId;
    private String passengerUsername;
    private SeatStatus status;
    private ZonedDateTime requestedAt;
}
