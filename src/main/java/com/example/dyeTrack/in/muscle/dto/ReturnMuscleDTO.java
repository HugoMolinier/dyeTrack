package com.example.dyeTrack.in.muscle.dto;

import com.example.dyeTrack.core.entity.Muscle;

public class ReturnMuscleDTO {

    private Long id;
    private String nameFR;
    private String nameEN;
    private Long idMuscularGroup;

    public ReturnMuscleDTO() {
    }

    public ReturnMuscleDTO(Muscle muscle) {
        this.id = muscle.getId();
        this.nameEN = muscle.getNameEN();
        this.nameFR = muscle.getNameFR();
        this.idMuscularGroup = muscle.getMuscleGroup().getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameFR() {
        return nameFR;
    }

    public void setNameFR(String nameFR) {
        this.nameFR = nameFR;
    }

    public String getNameEN() {
        return nameEN;
    }

    public void setNameEN(String nameEN) {
        this.nameEN = nameEN;
    }

    public Long getIdMuscularGroup() {
        return idMuscularGroup;
    }

    public void setIdMuscularGroup(Long idMuscularGroup) {
        this.idMuscularGroup = idMuscularGroup;
    }

}
