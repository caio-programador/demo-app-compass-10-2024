package com.example.demo.services.impl;

import com.example.demo.entities.ClientVaga;
import com.example.demo.repositories.ClientVagaRepository;
import com.example.demo.services.ClientVagaService;
import com.example.demo.web.dtos.ParkingResponseDTO;
import com.example.demo.web.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientVagaServiceImpl implements ClientVagaService {
    private final ClientVagaRepository repository;

    @Override
    public ClientVaga save(ClientVaga clientVaga) {
        return repository.save(clientVaga);
    }

    @Override
    public ParkingResponseDTO getByReceipt(String receipt) {
        ClientVaga clientVaga = repository.findByReceiptAndDepartureDateIsNull(receipt)
                .orElseThrow(() -> new EntityNotFoundException("Receipt number not found or check-out already done"));
        return new ParkingResponseDTO(
                clientVaga.getPlate(),
                clientVaga.getBrand(),
                clientVaga.getModel(),
                clientVaga.getColor(),
                clientVaga.getClient().getCpf(),
                clientVaga.getReceipt(),
                clientVaga.getEntryDate(),
                clientVaga.getDepartureDate(),
                clientVaga.getVaga().getCode(),
                clientVaga.getPrice(),
                clientVaga.getDiscount()
        );
    }

    @Override
    public long getTotalTimesCompleteParking(String cpf) {
        return repository.countByClientCpfAndDepartureDateIsNotNull(cpf);
    }

    @Override
    public Page<ParkingResponseDTO> findAllByCpf(String cpf, Pageable pageable) {
        Page<ClientVaga> clientVagaPage = repository.findAllByClientCpf(cpf, pageable)
                .orElseThrow(() -> new EntityNotFoundException("Client with this cpf not found"));
        return clientVagaPage.map(clientVaga -> new ParkingResponseDTO(
                clientVaga.getPlate(),
                clientVaga.getBrand(),
                clientVaga.getModel(),
                clientVaga.getColor(),
                clientVaga.getClient().getCpf(),
                clientVaga.getReceipt(),
                clientVaga.getEntryDate(),
                clientVaga.getDepartureDate(),
                clientVaga.getVaga().getCode(),
                clientVaga.getPrice(),
                clientVaga.getDiscount()
        ));
    }

    @Override
    public Page<ParkingResponseDTO> findAllByUserId(Long id, Pageable pageable) {
        Page<ClientVaga> clientVagaPage = repository.findAllByClientId(id, pageable)
                .orElseThrow(() -> new EntityNotFoundException("Client with this cpf not found"));
        return clientVagaPage.map(clientVaga -> new ParkingResponseDTO(
                clientVaga.getPlate(),
                clientVaga.getBrand(),
                clientVaga.getModel(),
                clientVaga.getColor(),
                clientVaga.getClient().getCpf(),
                clientVaga.getReceipt(),
                clientVaga.getEntryDate(),
                clientVaga.getDepartureDate(),
                clientVaga.getVaga().getCode(),
                clientVaga.getPrice(),
                clientVaga.getDiscount()
        ));
    }
}
