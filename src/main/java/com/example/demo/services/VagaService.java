package com.example.demo.services;

import com.example.demo.entities.Vaga;
import com.example.demo.web.dtos.VagaCreateDTO;
import com.example.demo.web.dtos.VagaResponseDTO;
import org.springframework.transaction.annotation.Transactional;

public interface VagaService {
    @Transactional
    VagaResponseDTO save(VagaCreateDTO vagaCreateDTO);
    @Transactional(readOnly = true)
    VagaResponseDTO findByCode(String code);
    @Transactional(readOnly = true)
    Vaga findByFreeVaga();
}
