package com.ejemplo.pc1.dtos;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class TripRequestDTO {
    @NotBlank
    private String origin;
    @NotBlank
    private String destination;
    private LocalDateTime depeartureTime;
    @NotNull
    @Min(1)@Max(6)
    private Integer capacity;
    @NotNull @Future
    private ZonedDateTime departureTime;
}
