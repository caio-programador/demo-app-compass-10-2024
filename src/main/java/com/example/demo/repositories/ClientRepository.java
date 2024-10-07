package com.example.demo.repositories;

import com.example.demo.entities.Client;
import com.example.demo.web.dtos.ClientResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByUserId(Long id);

    Optional<Client> findByCpf(String cpf);
}
