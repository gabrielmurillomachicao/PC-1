package com.ejemplo.pc1.events;

import java.time.ZonedDateTime;

public record SeatConfirmedEvent(
        Long seatRequestId,
        Long tripId,
        String passengerUsername,
        String driverUsername,
        Integer availableSeats,
        ZonedDateTime confirmedAt
) {
}
