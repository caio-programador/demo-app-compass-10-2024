package com.example.demo.services;

import com.example.demo.entities.ClientVaga;
import com.example.demo.web.dtos.ParkingResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface ClientVagaService {
    @Transactional
    ClientVaga save(ClientVaga clientVaga);
    @Transactional(readOnly = true)
    ParkingResponseDTO getByReceipt(String receipt);
    @Transactional(readOnly = true)
    long getTotalTimesCompleteParking(String cpf);
    @Transactional(readOnly = true)
    Page<ParkingResponseDTO> findAllByCpf(String cpf, Pageable pageable);
    @Transactional(readOnly = true)
    Page<ParkingResponseDTO> findAllByUserId(Long id, Pageable pageable);
}
