package com.ejemplo.pc1.dtos;

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
public class MyRideDTO {
    private String type;
    private Long tripId;
    private String status;
    private String origin;
    private String destination;
    private ZonedDateTime departureTime;
}
