package com.chemical.controllers;

import com.chemical.dto.request.RefreshTokenDTO;
import com.chemical.dto.response.LoginResponseDTO;
import com.chemical.dto.response.UserResponseDTO;
import jakarta.validation.Valid;
import com.chemical.common.BaseResponse;
import com.chemical.dto.request.AuthenticationDTO;
import com.chemical.dto.request.UserCreateRequestDTO;
import com.chemical.entity.User;
import com.chemical.config.security.TokenService;
import com.chemical.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Authentication Controller", description = "Endpoints for user authentication and registration")
@RestController
@RequestMapping(value = "/api/auth", produces = { "application/json" })
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    UserService userServices;
    AuthenticationManager authenticationManager;
    TokenService tokenService;

    /**
     * Authenticates user login.
     *
     * @param data Object containing user credentials
     * @return ResponseEntity containing authentication token
     */
    @Operation(summary = "Authenticate user and return tokens")
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse login(@RequestBody AuthenticationDTO data) {
        var credentials = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(credentials);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        var refreshToken = tokenService.generateRefreshToken((User) auth.getPrincipal());
        UserResponseDTO loggedInUser = userServices.findByEmailAuth(data.email());
        return BaseResponse.ok(new LoginResponseDTO(token, refreshToken, loggedInUser));
    }

    /**
     * Refreshes authentication token.
     *
     * @param refreshToken Object containing refresh token
     * @return ResponseEntity containing new authentication token
     */
    @Operation(summary = "Refresh authentication token")
    @PostMapping(value = "/refresh-token")
    public BaseResponse<String> refreshToken(@RequestBody RefreshTokenDTO refreshToken) {
        String newToken = tokenService.refreshToken(refreshToken.getRefreshToken());
        if (newToken == null) {
            return (BaseResponse.badRequest("Invalid refresh token"));
        }
        return BaseResponse.ok(newToken);
    }

    /**
     * Registers a new user.
     *
     * @param request Object containing user registration data
     * @return ResponseEntity indicating success or failure of registration
     */
    @Operation(summary = "Register a new user")
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<User> register(@Valid @RequestBody UserCreateRequestDTO request) {
        User savedUser = userServices.save(request);
        return BaseResponse.created(savedUser);
    }
}
