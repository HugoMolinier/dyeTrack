package com.example.dyeTrack.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Equipement {

    @Id
    @NotNull
    private Long id;

    @NotBlank
    private String nomFR;

    @NotBlank
    private String nomEN;

    public Equipement(Long id, String nomFR, String nomEN) {
        this.id = id;
        this.nomFR = nomFR;
        this.nomEN = nomEN;
    }

    protected Equipement() {
    }

    public Long getId() {
        return id;
    }

    public String getNomFR() {
        return nomFR;
    }

    public String getNomEN() {
        return nomEN;
    }

    @Override
    public String toString() {
        return "id " + this.id +
                " : nomFR " + this.nomFR;
    }
}
