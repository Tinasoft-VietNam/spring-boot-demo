package com.chemical.dto.response;

import com.chemical.entity.User;
import lombok.Data;

@Data
public class LoginResponseDTO {
    private String token;
    private String refresh_token;
    private UserResponseDTO user;

    public LoginResponseDTO(String token, String refresh_token, UserResponseDTO user) {
        this.token = token;
        this.refresh_token = refresh_token;
        this.user = user;
    }
}

