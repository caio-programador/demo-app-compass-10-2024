package com.example.demo.repositories;

import com.example.demo.entities.StatusVaga;
import com.example.demo.entities.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VagaRepository extends JpaRepository<Vaga, Long> {
    Optional<Vaga> findByCode(String code);

    Optional<Vaga> findFirstByStatus(StatusVaga status);
}
