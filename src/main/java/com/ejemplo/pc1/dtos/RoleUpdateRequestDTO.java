package com.ejemplo.pc1.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleUpdateRequestDTO {
    @NotBlank
    @Pattern(regexp = "ROLE_USER|ROLE_PASSENGER|ROLE_DRIVER|ROLE_ADMIN")
    private String role;
}
