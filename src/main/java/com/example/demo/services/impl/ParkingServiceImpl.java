package com.example.demo.services.impl;

import com.example.demo.entities.Client;
import com.example.demo.entities.ClientVaga;
import com.example.demo.entities.StatusVaga;
import com.example.demo.entities.Vaga;
import com.example.demo.repositories.ClientVagaRepository;
import com.example.demo.services.ClientService;
import com.example.demo.services.ClientVagaService;
import com.example.demo.services.ParkingService;
import com.example.demo.services.VagaService;
import com.example.demo.utils.ParkingUtils;
import com.example.demo.web.dtos.ParkingCreateDTO;
import com.example.demo.web.dtos.ParkingResponseDTO;
import com.example.demo.web.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ParkingServiceImpl implements ParkingService {
    private final ClientVagaService clientVagaService;
    private final ClientService clientService;
    private final VagaService vagaService;
    private final ClientVagaRepository clientVagaRepository;


    @Override
    public ParkingResponseDTO checkIn(ParkingCreateDTO parkingCreateDTO) {
        ClientVaga clientVaga = new ClientVaga();
        clientVaga.setPlate(parkingCreateDTO.plate());
        clientVaga.setBrand(parkingCreateDTO.brand());
        clientVaga.setModel(parkingCreateDTO.model());
        clientVaga.setColor(parkingCreateDTO.color());
        Client client = clientService.findByCpf(parkingCreateDTO.cpf());
        clientVaga.setClient(client);
        Vaga vaga = vagaService.findByFreeVaga();
        vaga.setStatus(StatusVaga.OCUPADA);
        clientVaga.setVaga(vaga);

        clientVaga.setEntryDate(LocalDateTime.now());
        clientVaga.setReceipt(ParkingUtils.generateParkingReceipt());
        clientVaga = clientVagaService.save(clientVaga);
        return new ParkingResponseDTO(
                clientVaga.getPlate(), clientVaga.getBrand(),
                clientVaga.getModel(), clientVaga.getColor(),
                clientVaga.getClient().getCpf(), clientVaga.getReceipt(),
                clientVaga.getEntryDate(), clientVaga.getDepartureDate(),
                clientVaga.getVaga().getCode(), clientVaga.getPrice(),
                clientVaga.getDiscount()
        );

    }

    @Override
    public ParkingResponseDTO checkout(String receipt) {
        ClientVaga clientVaga = clientVagaRepository.findByReceiptAndDepartureDateIsNull(receipt)
                .orElseThrow(()-> new EntityNotFoundException("Receipt not found or check-out already done"));
        LocalDateTime departureDate = LocalDateTime.now();
        BigDecimal price = ParkingUtils.calculatePrice(clientVaga.getEntryDate(), departureDate);
        clientVaga.setPrice(price);
        long times = clientVagaService.getTotalTimesCompleteParking(clientVaga.getClient().getCpf());
        clientVaga.setDiscount(ParkingUtils.calculateDiscount(price, times));
        clientVaga.setDepartureDate(departureDate);
        clientVaga.getVaga().setStatus(StatusVaga.LIVRE);
        clientVaga = clientVagaService.save(clientVaga);
        return new ParkingResponseDTO(
                clientVaga.getPlate(), clientVaga.getBrand(),
                clientVaga.getModel(), clientVaga.getColor(),
                clientVaga.getClient().getCpf(), clientVaga.getReceipt(),
                clientVaga.getEntryDate(), clientVaga.getDepartureDate(),
                clientVaga.getVaga().getCode(), clientVaga.getPrice(),
                clientVaga.getDiscount()
        );
    }
}
