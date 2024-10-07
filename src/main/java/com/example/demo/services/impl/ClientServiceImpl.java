package com.example.demo.services.impl;

import com.example.demo.entities.Client;
import com.example.demo.jwt.JwtUserDetails;
import com.example.demo.repositories.ClientRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.ClientService;
import com.example.demo.web.dtos.ClientCreateDTO;
import com.example.demo.web.dtos.ClientResponseDTO;
import com.example.demo.web.exceptions.EntityNotFoundException;
import com.example.demo.web.exceptions.UniqueFieldViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    @Override
    public ClientResponseDTO save(ClientCreateDTO clientCreateDTO, JwtUserDetails userDetails) {
        try {
            Client client = new Client();
            client.setName(clientCreateDTO.name());
            client.setCpf(clientCreateDTO.cpf());
            client.setUser(userRepository.findById(userDetails.getId()).orElseThrow(() -> new EntityNotFoundException("Incorrect user")));
            client = clientRepository.save(client);
            return new ClientResponseDTO(client.getId(), client.getName(), client.getCpf());
        }catch (DataIntegrityViolationException e){
            throw new UniqueFieldViolationException("Client with this cpf already exists");
        }
    }

    @Override
    public ClientResponseDTO findById(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Client with this id does not exist"));
        return new ClientResponseDTO(client.getId(), client.getName(), client.getCpf());
    }

    @Override
    public Page<ClientResponseDTO> findAll(Pageable pageable) {
        Page<Client> clients = clientRepository.findAll(pageable);
        return clients
                .map(x -> new ClientResponseDTO(x.getId(),x.getName(), x.getCpf()));

    }

    @Override
    public ClientResponseDTO getDetails(JwtUserDetails userDetails) {
        Client client =  clientRepository.findByUserId(userDetails.getId());
        return new ClientResponseDTO(client.getId(), client.getName(), client.getCpf());
    }

    @Override
    public Client findByCpf(String cpf) {
        return clientRepository.findByCpf(cpf).orElseThrow(
                () -> new EntityNotFoundException("Client with this cpf does not exists"));
    }
}
