package com.example.demo.web.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ParkingResponseDTO(
        String plate,
        String brand,
        String model,
        String color,
        String cpf,
        String receipt,
        @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
        LocalDateTime entryDate,
        @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
        LocalDateTime departureDate,
        String vagaCode,
        BigDecimal price,
        BigDecimal discount
) {
}
