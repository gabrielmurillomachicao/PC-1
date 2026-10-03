package com.ejemplo.pc1.Service;

import com.ejemplo.pc1.Config.JwtService;
import com.ejemplo.pc1.Repository.tripRepository;
import com.ejemplo.pc1.Repository.userRepository;
import com.ejemplo.pc1.dtos.MyRideDTO;
import com.ejemplo.pc1.dtos.PageResponseDTO;
import com.ejemplo.pc1.dtos.RegisterRequestDTO;
import com.ejemplo.pc1.dtos.RegisterResponseDTO;
import com.ejemplo.pc1.dtos.TripRequestDTO;
import com.ejemplo.pc1.dtos.TripResponseDTO;
import com.ejemplo.pc1.excepctions.ForbiddenTripActionException;
import com.ejemplo.pc1.excepctions.TripNotFoundException;
import com.ejemplo.pc1.excepctions.TripOverlapException;
import com.ejemplo.pc1.model.SeatStatus;
import com.ejemplo.pc1.model.TripStatus;
import com.ejemplo.pc1.model.route;
import com.ejemplo.pc1.model.trip;
import com.ejemplo.pc1.model.user;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripService {
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final userRepository userRepository;
    private final tripRepository tripRepository;
    private final com.ejemplo.pc1.Repository.seatRequest seatRequestRepository;
    private final RouteService routeService;

    public static final Duration TRIP_WINDOW = Duration.ofHours(2);
    public static final List<TripStatus> ACTIVE_STATUSES = List.of(TripStatus.SCHEDULED, TripStatus.FULL);

    public RegisterResponseDTO postrip(RegisterRequestDTO dto){

        return new RegisterResponseDTO();
    }
    private user getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        user user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encopntrado"));
        return user;
    }

    @Transactional
    public TripResponseDTO createTrip(TripRequestDTO dto) {
        user driver = getCurrentUser();

        if (!"ROLE_DRIVER".equals(driver.getRole())) {
            throw new ForbiddenTripActionException("Solo un conductor puede publicar viajes");
        }
        if (dto.getDepartureTime() == null || !dto.getDepartureTime().isAfter(ZonedDateTime.now())) {
            throw new IllegalArgumentException("La fecha de salida debe ser futura");
        }
        if (dto.getOrigin().trim().equalsIgnoreCase(dto.getDestination().trim())) {
            throw new IllegalArgumentException("El origen y el destino deben ser diferentes");
        }
        if (dto.getCapacity() == null || dto.getCapacity() < 1 || dto.getCapacity() > 6) {
            throw new IllegalArgumentException("La capacidad debe estar entre 1 y 6");
        }

        ZonedDateTime start = dto.getDepartureTime().minus(TRIP_WINDOW);
        ZonedDateTime end = dto.getDepartureTime().plus(TRIP_WINDOW);
        if (tripRepository.existsDriverOverlap(driver.getId(), ACTIVE_STATUSES, start, end)) {
            throw new TripOverlapException("Ya tienes un viaje publicado que se cruza con ese horario");
        }
        if (seatRequestRepository.existsPassengerOverlap(driver.getId(), -1L, SeatStatus.CONFIRMED, start, end)) {
            throw new TripOverlapException("Tienes un asiento reservado en un viaje que se cruza con ese horario");
        }

        route newRoute = routeService.createRoute(driver, dto.getOrigin(), dto.getDestination());

        trip newTrip = trip.builder()
                .route(newRoute)
                .departureTime(dto.getDepartureTime())
                .capacity(dto.getCapacity())
                .availableSeats(dto.getCapacity())
                .tripStatus(TripStatus.SCHEDULED)
                .build();

        trip saved = tripRepository.save(newTrip);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<TripResponseDTO> searchTrips(String origin, String destination,
                                                        ZonedDateTime from, int page, int size) {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime fromDate = (from == null || from.isBefore(now)) ? now : from;
        String originFilter = origin == null ? "" : origin.trim();
        String destinationFilter = destination == null ? "" : destination.trim();

        Pageable pageable = PageRequest.of(Math.max(page, 0), size <= 0 ? 10 : size,
                Sort.by(Sort.Direction.ASC, "departureTime"));

        Page<TripResponseDTO> result = tripRepository
                .search(ACTIVE_STATUSES, now, fromDate, originFilter, destinationFilter, pageable)
                .map(this::toResponse);

        return new PageResponseDTO<>(result);
    }

    @Transactional
    public TripResponseDTO cancelTrip(Long tripId) {
        user current = getCurrentUser();
        trip found = tripRepository.findByIdForUpdate(tripId)
                .orElseThrow(() -> new TripNotFoundException("Viaje no encontrado"));

        if (!found.getRoute().getDriverUser().getId().equals(current.getId())) {
            throw new ForbiddenTripActionException("No puedes modificar un viaje que no es tuyo");
        }
        if (found.getTripStatus() == TripStatus.CANCELLED || found.getTripStatus() == TripStatus.COMPLETED) {
            throw new ForbiddenTripActionException("El viaje ya no se puede cancelar");
        }

        found.setTripStatus(TripStatus.CANCELLED);
        seatRequestRepository.findByTrip_Id(found.getId())
                .forEach(request -> request.setSeatStatus(SeatStatus.CANCELLED));
        return toResponse(tripRepository.save(found));
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<MyRideDTO> myRides(String type, int page, int size) {
        user current = getCurrentUser();
        String filter = type == null ? "all" : type.trim().toLowerCase();
        if (!filter.equals("all") && !filter.equals("driver") && !filter.equals("passenger")) {
            throw new IllegalArgumentException("type debe ser all, driver o passenger");
        }

        List<MyRideDTO> rides = new ArrayList<>();

        if (filter.equals("all") || filter.equals("driver")) {
            tripRepository.findByRoute_DriverUser_Id(current.getId()).forEach(t -> rides.add(
                    MyRideDTO.builder()
                            .type("DRIVER")
                            .tripId(t.getId())
                            .status(t.getTripStatus().name())
                            .origin(t.getRoute().getOrigin())
                            .destination(t.getRoute().getDestination())
                            .departureTime(t.getDepartureTime())
                            .build()));
        }

        if (filter.equals("all") || filter.equals("passenger")) {
            seatRequestRepository.findByPassenger_Id(current.getId()).forEach(s -> rides.add(
                    MyRideDTO.builder()
                            .type("PASSENGER")
                            .tripId(s.getTrip().getId())
                            .status(s.getSeatStatus().name())
                            .origin(s.getTrip().getRoute().getOrigin())
                            .destination(s.getTrip().getRoute().getDestination())
                            .departureTime(s.getTrip().getDepartureTime())
                            .build()));
        }

        rides.sort(Comparator.comparing(MyRideDTO::getDepartureTime).reversed());

        int safeSize = size <= 0 ? 10 : size;
        int safePage = Math.max(page, 0);
        int fromIndex = Math.min(safePage * safeSize, rides.size());
        int toIndex = Math.min(fromIndex + safeSize, rides.size());

        Page<MyRideDTO> result = new PageImpl<>(rides.subList(fromIndex, toIndex),
                PageRequest.of(safePage, safeSize), rides.size());
        return new PageResponseDTO<>(result);
    }

    public TripResponseDTO toResponse(trip t) {
        return TripResponseDTO.builder()
                .id(t.getId())
                .driverUsername(t.getRoute().getDriverUser().getUsername())
                .origin(t.getRoute().getOrigin())
                .destination(t.getRoute().getDestination())
                .availableSeats(t.getAvailableSeats())
                .status(t.getTripStatus())
                .departureTime(t.getDepartureTime())
                .capacity(t.getCapacity())
                .build();
    }

}
