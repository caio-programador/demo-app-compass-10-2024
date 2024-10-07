package com.example.demo.repositories;

import com.example.demo.entities.ClientVaga;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientVagaRepository extends JpaRepository<ClientVaga, Long> {
    Optional<ClientVaga> findByReceiptAndDepartureDateIsNull(String receipt);

    long countByClientCpfAndDepartureDateIsNotNull(String cpf);

    Optional<Page<ClientVaga>> findAllByClientCpf(String cpf, Pageable pageable);

    Optional<Page<ClientVaga>> findAllByClientId(Long id, Pageable pageable);
}
