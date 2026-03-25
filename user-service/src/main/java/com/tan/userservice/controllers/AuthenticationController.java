package com.tan.userservice.controllers;

import com.tan.userservice.common.BaseResponse;
import com.tan.userservice.dto.request.AuthenticationDTO;
import com.tan.userservice.dto.request.RefreshTokenDTO;
import com.tan.userservice.dto.request.UserCreateRequestDTO;
import com.tan.userservice.dto.response.LoginResponseDTO;
import com.tan.userservice.dto.response.UserResponseDTO;
import com.tan.userservice.entity.User;
import com.tan.userservice.services.UserService;
import com.tan.userservice.security.TokenService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
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
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<LoginResponseDTO> login(@RequestBody AuthenticationDTO data) {
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
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<User> register(@Valid @RequestBody UserCreateRequestDTO request) {
        User savedUser = userServices.save(request);
        return BaseResponse.created(savedUser);
    }
}
