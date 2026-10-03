package com.ejemplo.pc1.Service;

import com.ejemplo.pc1.Config.JwtService;
import com.ejemplo.pc1.Repository.userRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RouteService {
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final userRepository userRepository;
}
