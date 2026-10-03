package com.ejemplo.pc1.Service;

import com.ejemplo.pc1.Config.JwtService;
import com.ejemplo.pc1.Repository.routeRepository;
import com.ejemplo.pc1.Repository.userRepository;
import com.ejemplo.pc1.model.Routestatus;
import com.ejemplo.pc1.model.route;
import com.ejemplo.pc1.model.user;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RouteService {
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final userRepository userRepository;
    private final routeRepository routeRepository;

    @Transactional
    public route createRoute(user driver, String origin, String destination) {
        route newRoute = route.builder()
                .driverUser(driver)
                .origin(origin.trim())
                .destination(destination.trim())
                .status(Routestatus.ACTIVE)
                .build();
        return routeRepository.save(newRoute);
    }
}
