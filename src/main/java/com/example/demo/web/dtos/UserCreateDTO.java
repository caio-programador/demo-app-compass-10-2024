package com.example.demo.web.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateDTO(
        @Email(message = "The email format is invalid")
        @NotBlank
        String username,

        @NotBlank
        @Size(min = 6, message = "The password shoud be at least 6 characters")
        String password) {}
