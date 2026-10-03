package com.ejemplo.pc1.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponseDTO {
    private Long id;
    private String username;
    private String email;

}
