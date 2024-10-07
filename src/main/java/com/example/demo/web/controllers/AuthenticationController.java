package com.example.demo.web.controllers;

import com.example.demo.jwt.JwtToken;
import com.example.demo.jwt.JwtUserDetailsService;
import com.example.demo.services.UserService;
import com.example.demo.web.dtos.UserLoginDTO;
import com.example.demo.web.exceptions.InvalidCredentialsException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Authentication",
        description = "There are all authenticate operation"
)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {
    private final JwtUserDetailsService detailsService;
    private final AuthenticationManager authenticationManager;


    @Operation(
            summary = "Login into API",
            description = "Feature of authentication at API",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully authenticate",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JwtToken.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid Credentials",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "422", description = "Unprocessable entity",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            }
    )
    @PostMapping("/auth")
    public ResponseEntity<JwtToken> authenticate(@RequestBody @Valid UserLoginDTO userLoginDTO,
                                   HttpServletRequest request
    ) {
        try{
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userLoginDTO.username(), userLoginDTO.password());
            authenticationManager.authenticate(authenticationToken);
            JwtToken jwtToken = detailsService.getTokenAuthenticate(userLoginDTO.username());
            return ResponseEntity.ok(jwtToken);
        }catch (AuthenticationException e){
            throw new InvalidCredentialsException("Invalid username or password.");
        }
    }
}
