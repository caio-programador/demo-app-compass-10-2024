package com.example.demo.services;


import com.example.demo.entities.Client;
import com.example.demo.jwt.JwtUserDetails;
import com.example.demo.web.dtos.ClientCreateDTO;
import com.example.demo.web.dtos.ClientResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface ClientService {
    @Transactional
    ClientResponseDTO save(ClientCreateDTO clientCreateDTO, JwtUserDetails userDetails);
    @Transactional(readOnly = true)
    ClientResponseDTO findById(Long id);
    @Transactional(readOnly = true)
    Page<ClientResponseDTO> findAll(Pageable pageable);
    @Transactional (readOnly = true)
    ClientResponseDTO getDetails(JwtUserDetails userDetails);

    Client findByCpf(String cpf);
}
