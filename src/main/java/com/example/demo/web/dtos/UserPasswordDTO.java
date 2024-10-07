package com.example.demo.web.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserPasswordDTO (
        @NotBlank
        String password,
        @Size(min = 6, message = "The new password shoud be at least 6 characters")
        @NotBlank
        String newPassword
) {
}
