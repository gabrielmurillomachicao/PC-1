package com.ejemplo.pc1.dtos;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class TripRequestDTO {
    private String origin;
    private String destination;
    private LocalDateTime depeartureTime;
    @Min(1)@Max(6)
    private Integer capacity;
}
