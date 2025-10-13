package com.example.dyeTrack.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class MuscleGroup {

    @Id
    @NotNull
    private Long id;

    @NotBlank
    private String nameFR;

    @NotBlank
    private String nameEN;

    public MuscleGroup(Long id, String nameFR, String nameEN) {
        this.nameFR = nameFR;
        this.nameEN = nameEN;
        this.id = id;
    }

    protected MuscleGroup() {
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
        return "idMuscleGroup " + this.id +
                " : nameFR " + this.nameFR;
    }
}
