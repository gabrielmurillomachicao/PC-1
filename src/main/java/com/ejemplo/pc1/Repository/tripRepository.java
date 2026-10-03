package com.ejemplo.pc1.Repository;

import com.ejemplo.pc1.model.route;
import com.ejemplo.pc1.model.trip;
import com.ejemplo.pc1.model.TripStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface tripRepository extends JpaRepository<trip,Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM trip t WHERE t.id = :id")
    Optional<trip> findByIdForUpdate(@Param("id") Long id);

    List<trip> findByRoute_DriverUser_Id(Long driverId);

    @Query("""
        SELECT COUNT(t) > 0 FROM trip t
        WHERE t.route.driverUser.id = :driverId
          AND t.tripStatus IN :statuses
          AND t.departureTime > :start
          AND t.departureTime < :end
        """)
    boolean existsDriverOverlap(@Param("driverId") Long driverId,
                                @Param("statuses") Collection<TripStatus> statuses,
                                @Param("start") ZonedDateTime start,
                                @Param("end") ZonedDateTime end);

    @Query("""
        SELECT t FROM trip t
        WHERE t.tripStatus IN :statuses
          AND t.departureTime > :now
          AND t.departureTime >= :fromDate
          AND LOWER(t.route.origin) LIKE LOWER(CONCAT('%', :origin, '%'))
          AND LOWER(t.route.destination) LIKE LOWER(CONCAT('%', :destination, '%'))
        """)
    Page<trip> search(@Param("statuses") Collection<TripStatus> statuses,
                      @Param("now") ZonedDateTime now,
                      @Param("fromDate") ZonedDateTime fromDate,
                      @Param("origin") String origin,
                      @Param("destination") String destination,
                      Pageable pageable);
}
