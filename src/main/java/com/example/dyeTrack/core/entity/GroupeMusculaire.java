package com.example.dyeTrack.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class GroupeMusculaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nomFR;

    @NotBlank
    private String nomEN;

    public GroupeMusculaire(String nomFR, String nomEN) {
        this.nomFR = nomFR;
        this.nomEN = nomEN;
    }

    protected GroupeMusculaire() {
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
        return "idGroupeMusculaire " + this.id +
                " : nomFR " + this.nomFR;
    }
}
