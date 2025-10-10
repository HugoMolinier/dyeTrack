package com.example.dyeTrack.in.user.dto;

import com.example.dyeTrack.core.valueobject.AuthValue;

import io.swagger.v3.oas.annotations.media.Schema;

public class ReturnUserTokenDTO {

    private UserDTO userDTO;

    @Schema(description = "Token JWT de l'utilisateur", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")
    private String token;

    // Constructeur principa
    public ReturnUserTokenDTO(UserDTO userDTO, String token) {
        this.userDTO = userDTO;
        this.token = token;
    }

    public ReturnUserTokenDTO(AuthValue authValue) {
        this.token = authValue.getToken();
        this.userDTO = new UserDTO(authValue.getUser());
    }

    public ReturnUserTokenDTO() {
    }

    // Getters
    public UserDTO getUserDTO() {
        return userDTO;
    }

    public String getToken() {
        return token;
    }
}
