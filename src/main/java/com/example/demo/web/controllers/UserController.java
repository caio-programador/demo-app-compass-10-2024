package com.example.demo.web.controllers;

import com.example.demo.services.UserService;
import com.example.demo.web.dtos.UserCreateDTO;
import com.example.demo.web.dtos.UserPasswordDTO;
import com.example.demo.web.dtos.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(
        name = "User Controller",
        description = "CRUD for User entity"

)
@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Create a new User",
            description = "The function to create a new user",
            responses = {
                    @ApiResponse(responseCode = "201", content = @Content(mediaType = "application/json"
                    , schema = @Schema(implementation = UserResponseDTO.class))),
                    @ApiResponse(responseCode = "409",description = "Username already exists",
                            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "422", description = "Invalid form",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping()
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateDTO user) {
        UserResponseDTO newUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }


    @Operation(
            summary = "Get one User by id",
            description = "The function to return a user by id",
            security = @SecurityRequirement(
                    name = "security"
            ),
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"
                            , schema = @Schema(implementation = UserResponseDTO.class))),
                    @ApiResponse(responseCode = "404",description = "User does not exists",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403",description = "User without permission",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') OR (hasRole('CLIENT') AND #id == authentication.principal.id)")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }
    @Operation(
            summary = "Create a new User",
            description = "The function to create a new user",
            security = @SecurityRequirement(
                    name = "security"
            ),
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"
                            , schema = @Schema(implementation = List.class))),
                    @ApiResponse(responseCode = "403",description = "User without permission",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }
    @Operation(
            summary = "Change Password",
            description = "The function to change a specific User's password",
            security = @SecurityRequirement(
                    name = "security"
            ),
            responses = {
                    @ApiResponse(responseCode = "204", description = "No content"),
                    @ApiResponse(responseCode = "400", description = "Invalid password",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "422", description = "Invalid new password",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403",description = "User without permission",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT') AND #id == authentication.principal.id")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @Valid @RequestBody UserPasswordDTO user) {
        userService.changePassword(user, id);
        return ResponseEntity.noContent().build();
    }
    @Operation(
            summary = "Delete a User",
            description = "The function to delete a user",
            security = @SecurityRequirement(
                    name = "security"
            ),
            responses = {
                    @ApiResponse(responseCode = "204", description = "No content"),
                    @ApiResponse(responseCode = "404", description = "User does not exists",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403",description = "User without permission",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
