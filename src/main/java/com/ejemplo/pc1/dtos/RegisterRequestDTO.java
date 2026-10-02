package com.ejemplo.pc1.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class RegisterRequestDTO {
    @NotBlank
    private String username;
    @NotBlank @Size(min = 8)
    private String password;
}
