package com.ejemplo.pc1.Repository;

import com.ejemplo.pc1.model.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

public interface seatRequest extends JpaRepository<com.ejemplo.pc1.model.seatRequest,Long> {

    boolean existsByTrip_IdAndPassenger_Id(Long tripId, Long passengerId);

    List<com.ejemplo.pc1.model.seatRequest> findByPassenger_Id(Long passengerId);

    List<com.ejemplo.pc1.model.seatRequest> findByTrip_Id(Long tripId);

    @Query("""
        SELECT COUNT(s) > 0 FROM seatRequest s
        WHERE s.passenger.id = :passengerId
          AND s.seatStatus = :status
          AND s.trip.id <> :tripId
          AND s.trip.departureTime > :start
          AND s.trip.departureTime < :end
        """)
    boolean existsPassengerOverlap(@Param("passengerId") Long passengerId,
                                   @Param("tripId") Long tripId,
                                   @Param("status") SeatStatus status,
                                   @Param("start") ZonedDateTime start,
                                   @Param("end") ZonedDateTime end);
}
