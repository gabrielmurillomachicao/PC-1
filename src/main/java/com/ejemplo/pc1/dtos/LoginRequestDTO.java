package com.ejemplo.pc1.dtos;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class LoginRequestDTO {
    @NotBlank
    private String username;
    @NotBlank @Size(min = 8)
    private String password;
}
