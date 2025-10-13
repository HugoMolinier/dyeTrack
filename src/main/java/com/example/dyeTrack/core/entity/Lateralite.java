package com.example.dyeTrack.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Lateralite {

    @Id
    @NotNull
    private Long id;

    @NotBlank
    private String nameFR;

    @NotBlank
    private String nameEN;

    public Lateralite(Long id, String nameFR, String nameEN) {
        this.id = id;
        this.nameFR = nameFR;
        this.nameEN = nameEN;
    }

    protected Lateralite() {
    }

    public Long getId() {
        return id;
    }

    public String getNameFR() {
        return nameFR;
    }

    public String getNameEN() {
        return nameEN;
    }

    @Override
    public String toString() {
        return "id " + this.id +
                " : nameFR " + this.nameFR;
    }
}
