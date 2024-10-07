package com.example.demo.web.controllers;

import com.example.demo.jwt.JwtUserDetails;
import com.example.demo.services.ClientVagaService;
import com.example.demo.services.ParkingService;
import com.example.demo.web.dtos.ParkingCreateDTO;
import com.example.demo.web.dtos.ParkingResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Parking Controller",
        description = "CRUD for ClientVaga entity"
)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/parking")
public class ParkingController {
    private final ParkingService parkingService;
    private final ClientVagaService clientVagaService;


    @Operation(
            summary = "Check-in into a vaga",
            description = "The function to check-in",
            responses = {
                    @ApiResponse(responseCode = "201", content = @Content(mediaType = "application/json"
                            , schema = @Schema(implementation = ParkingResponseDTO.class))),
                    @ApiResponse(responseCode = "404",description = "There is no free vaga at the moment<br/>or<br/>" +
                            "There is no client with this cpf",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "422", description = "Invalid form",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403",description = "User without permission",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("/check-in")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingResponseDTO> checkIn(@RequestBody @Valid ParkingCreateDTO parkingCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingService.checkIn(parkingCreateDTO));
    }

    @Operation(
            summary = "Get information about one receipt",
            description = "The function to get information about one receipt",
            responses = {
                    @ApiResponse(responseCode = "201", content = @Content(mediaType = "application/json"
                            , schema = @Schema(implementation = ParkingResponseDTO.class))),
                    @ApiResponse(responseCode = "404",description = "There is no receipt or check-out already done",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("/check-in/{receipt}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    public ResponseEntity<ParkingResponseDTO> getByReceipt(@PathVariable String receipt) {
        return ResponseEntity.ok(clientVagaService.getByReceipt(receipt));
    }

    @Operation(
            summary = "Check-out ",
            description = "The function to check-out",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"
                            , schema = @Schema(implementation = ParkingResponseDTO.class))),
                    @ApiResponse(responseCode = "404",description = "Receipt not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403",description = "User without permission",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PutMapping("/check-in/{receipt}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingResponseDTO> checkOut(@PathVariable String receipt) {
        return ResponseEntity.ok(parkingService.checkout(receipt));
    }


    @Operation(
            summary = "Get parking by cpf",
            description = "Get all parking from a specific Client",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"
                            , schema = @Schema(implementation = ParkingResponseDTO.class))),
                    @ApiResponse(responseCode = "403",description = "User without permission",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Page<ParkingResponseDTO>> getAllParkingByCpf(@PathVariable String cpf, Pageable pageable) {
        return ResponseEntity.ok(clientVagaService.findAllByCpf(cpf, pageable));
    }

    @Operation(
            summary = "Get parking details",
            description = "Get all parking from a specific Client",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"
                            , schema = @Schema(implementation = ParkingResponseDTO.class))),
                    @ApiResponse(responseCode = "403",description = "User without permission",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping
    public ResponseEntity<Page<ParkingResponseDTO>> getAllParkingOfClient(@AuthenticationPrincipal JwtUserDetails userDetails, Pageable pageable) {
        return ResponseEntity.ok(clientVagaService.findAllByUserId(userDetails.getId(), pageable));
    }
}
