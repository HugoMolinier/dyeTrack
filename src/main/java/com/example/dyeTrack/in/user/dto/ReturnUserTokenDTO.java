package com.example.dyeTrack.in.user.dto;

import com.example.dyeTrack.core.valueobject.AuthValue;

public class ReturnUserTokenDTO {

    private UserDTO userDTO;
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
