package com.example.demo.web.controllers;


import com.example.demo.services.VagaService;
import com.example.demo.web.dtos.UserResponseDTO;
import com.example.demo.web.dtos.VagaCreateDTO;
import com.example.demo.web.dtos.VagaResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/vagas")
public class VagaController {
    private final VagaService vagaService;

    @Operation(
            summary = "Create a new Vaga",
            description = "The function to create a new vaga",
            responses = {
                    @ApiResponse(responseCode = "201", content = @Content(mediaType = "application/json"
                            , schema = @Schema(implementation = VagaResponseDTO.class))),
                    @ApiResponse(responseCode = "409",description = "Vaga with this code already exists",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "422", description = "Invalid form",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Client can not do this",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VagaResponseDTO> create(@RequestBody @Valid VagaCreateDTO vaga){
        return ResponseEntity.status(HttpStatus.CREATED).body(vagaService.save(vaga));
    }


    @Operation(
            summary = "Get one Vaga",
            description = "The function to get a vaga by code",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"
                            , schema = @Schema(implementation = VagaResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Vaga with this code does not exist",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Client can not do this",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VagaResponseDTO> getById(@PathVariable String code){
        return ResponseEntity.ok(vagaService.findByCode(code));
    }
}
