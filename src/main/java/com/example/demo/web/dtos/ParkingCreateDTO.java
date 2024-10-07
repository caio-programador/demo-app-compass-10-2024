package com.example.demo.web.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record ParkingCreateDTO(
        @NotBlank
        @Size(min = 7, max = 7)
        @Pattern(regexp = "[A-Z]{3}[0-9][A-Z][0-9]{2}", message = "The plate shoud be like 'XXX0X00'")
        String plate,
        @NotBlank
        String brand,
        @NotBlank
        String model,
        @NotBlank
        String color,
        @NotBlank
        @Size(min = 11, max = 11)
        @CPF
        String cpf
) {
}
