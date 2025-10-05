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
    private LocalDate dateNaissance;

    @Min(value = 50, message = "Taille trop petite")
    @Max(value = 300, message = "Taille trop grande")
    private Integer taille;
    private Boolean sexeMale;

    // Getters et setters
    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public Integer getTaille() {
        return taille;
    }

    public void setTaille(Integer taille) {
        this.taille = taille;
    }

    public Boolean getSexeMale() {
        return sexeMale;
    }

    public void setSexeMale(Boolean sexeMale) {
        this.sexeMale = sexeMale;
    }
}
