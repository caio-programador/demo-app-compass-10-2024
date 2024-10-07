package com.example.demo.services.impl;

import com.example.demo.entities.StatusVaga;
import com.example.demo.entities.Vaga;
import com.example.demo.repositories.VagaRepository;
import com.example.demo.services.VagaService;
import com.example.demo.web.dtos.VagaCreateDTO;
import com.example.demo.web.dtos.VagaResponseDTO;
import com.example.demo.web.exceptions.EntityNotFoundException;
import com.example.demo.web.exceptions.UniqueFieldViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VagaServiceImpl implements VagaService {
    private final VagaRepository vagaRepository;

    @Override
    public VagaResponseDTO save(VagaCreateDTO vagaCreateDTO) {
        try {
            Vaga vaga = new Vaga();
            vaga.setCode(vagaCreateDTO.code());
            vaga.setStatus(StatusVaga.valueOf(vagaCreateDTO.status()));
            vagaRepository.save(vaga);
            return new VagaResponseDTO(vaga.getId(), vaga.getCode(), vaga.getStatus().toString());
        }catch (DataIntegrityViolationException e){
            throw new UniqueFieldViolationException("Vaga with this code already exists");
        }
    }

    @Override
    public VagaResponseDTO findByCode(String code) {
        Vaga vaga = vagaRepository.findByCode(code).orElseThrow(() -> new EntityNotFoundException("Vaga with this code does not exist"));
        return new VagaResponseDTO(vaga.getId(), vaga.getCode(), vaga.getStatus().toString());

    }

    @Override
    public Vaga findByFreeVaga() {
        return vagaRepository.findFirstByStatus(StatusVaga.LIVRE).orElseThrow(
                () -> new EntityNotFoundException("There is no free vaga at the moment"));
    }
}
