package com.ejemplo.pc1.Service;

import com.ejemplo.pc1.Config.JwtService;
import com.ejemplo.pc1.Repository.userRepository;
import com.ejemplo.pc1.dtos.RegisterRequestDTO;
import com.ejemplo.pc1.dtos.RegisterResponseDTO;
import com.ejemplo.pc1.model.user;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TripService {
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final userRepository userRepository;
    public RegisterResponseDTO postrip(RegisterRequestDTO dto){

        return new RegisterResponseDTO();
    }
    private user getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        user user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encopntrado"));
        return user;
    }

}
