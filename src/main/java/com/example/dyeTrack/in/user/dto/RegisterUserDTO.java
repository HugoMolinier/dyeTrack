package com.example.dyeTrack.in.user.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class RegisterUserDTO extends LoginUserDTO {

    @NotBlank(message = "Pseudo is required")
    private String pseudo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthdate;

    @Min(value = 50, message = "Height trop petite")
    @Max(value = 300, message = "Height trop grande")
    private Integer height;
    private Boolean sexeMale;

    // Getters et setters
    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Boolean getSexeMale() {
        return sexeMale;
    }

    public void setSexeMale(Boolean sexeMale) {
        this.sexeMale = sexeMale;
    }
}
