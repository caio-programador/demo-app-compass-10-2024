package com.example.demo.web.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record VagaCreateDTO(
        @NotBlank
        String code,
        @NotBlank
        @Pattern(regexp = "LIVRE|OCUPADA")
        String status
) {
}
