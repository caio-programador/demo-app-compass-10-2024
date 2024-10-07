package com.example.demo.web.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginDTO(@NotBlank @Email String username,
                           @NotBlank @Size(min=6) String password) {
}
