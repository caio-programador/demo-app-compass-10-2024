package com.example.demo.services;

import com.example.demo.entities.ClientVaga;
import com.example.demo.web.dtos.ParkingCreateDTO;
import com.example.demo.web.dtos.ParkingResponseDTO;
import org.springframework.transaction.annotation.Transactional;

public interface ParkingService {
    @Transactional
    ParkingResponseDTO checkIn(ParkingCreateDTO parkingCreateDTO);
    @Transactional
    ParkingResponseDTO checkout(String receipt);
}
