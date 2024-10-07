package com.example.demo.web.controllers;

import com.example.demo.jwt.JwtUserDetails;
import com.example.demo.services.ClientService;
import com.example.demo.web.dtos.ClientCreateDTO;
import com.example.demo.web.dtos.ClientResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Clients", description = "All clients' operations")
@RequiredArgsConstructor
@RequestMapping("api/v1/clients")
@RestController
public class ClientController {
    private final ClientService clientService;


    @Operation(
            summary = "Create a new client", description = "Create a new client with a user",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully created user",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "409", description = "Client with this cpf already exists",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(responseCode = "422", description = "Bad credentials",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Admin can not do this",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    ),
            }
    )
    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ClientResponseDTO> create(@RequestBody @Valid ClientCreateDTO clientCreateDTO,
                                                    @AuthenticationPrincipal JwtUserDetails userDetails) {
        ClientResponseDTO client = clientService.save(clientCreateDTO, userDetails);
        return ResponseEntity.status(201).body(client);
    }



    @Operation(
            summary = "Find one client", description = "Find a client by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Client not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Client can not do this",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    ),
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(clientService.findById(id));
    }


    @Operation(
            summary = "Find all clients", description = "Find all clients of application",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Client can not do this",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    ),
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<ClientResponseDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(clientService.findAll(pageable));
    }

    @Operation(
            summary = "Get Details of client", description = "Get Details of client",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Admin can not do this",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    ),
            }
    )
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/details")
    public ResponseEntity<ClientResponseDTO> getDetails(@AuthenticationPrincipal JwtUserDetails userDetails) {
        return ResponseEntity.ok(clientService.getDetails(userDetails));
    }

}
