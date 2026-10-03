package com.ejemplo.pc1.Service;

import com.ejemplo.pc1.Config.JwtService;
import com.ejemplo.pc1.Repository.userRepository;
import com.ejemplo.pc1.dtos.LoginRequestDTO;
import com.ejemplo.pc1.dtos.LoginResponseDTO;
import com.ejemplo.pc1.dtos.*;
import com.ejemplo.pc1.excepctions.InvalidCredentialsException;
import com.ejemplo.pc1.excepctions.UserAlreadyExistsException;
import com.ejemplo.pc1.model.user;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final userRepository userRepository;
    @Transactional
    public RegisterResponseDTO register(RegisterRequestDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new UserAlreadyExistsException("El username ya existe");
        }
        if(userRepository.existsByEmail(dto.getEmail())){
            throw new UserAlreadyExistsException("El email ya existe");
        }
        user user = modelMapper.map(dto, user.class);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user saved =userRepository.save(user);
        RegisterResponseDTO responseDTO=RegisterResponseDTO.builder().id(saved.getId()).username(saved.getUsername()).email(saved.getEmail()).build();
        return responseDTO;
    }
    public LoginResponseDTO login( LoginRequestDTO dto){
        user user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(()-> new RuntimeException("Credenciales Incorrectas"));
        if(!passwordEncoder.matches(dto.getPassword(), user.getPassword())){
            throw new InvalidCredentialsException("Credenciales incorrectas");
        }
        String token= jwtService.generateToken(user.getUsername());
        return new LoginResponseDTO(token,3600L);
    }
}