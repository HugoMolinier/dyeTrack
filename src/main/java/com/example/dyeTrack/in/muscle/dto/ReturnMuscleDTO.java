package com.example.dyeTrack.in.muscle.dto;

import com.example.dyeTrack.core.entity.Muscle;

public class ReturnMuscleDTO {

    private Long idMuscle;
    private String nameFR;
    private String nameEN;
    private Long idMuscularGroup;

    public ReturnMuscleDTO() {
    }

    public ReturnMuscleDTO(Muscle muscle) {
        this.idMuscle = muscle.getId();
        this.nameEN = muscle.getNameEN();
        this.nameFR = muscle.getNameFR();
        this.idMuscularGroup = muscle.getMuscleGroup().getId();
    }

    public Long getIdMuscle() {
        return idMuscle;
    }

    public void setIdMuscle(Long idMuscle) {
        this.idMuscle = idMuscle;
    }

    public String getNameFr() {
        return nameFR;
    }

    public void setNameFr(String nameFr) {
        this.nameFR = nameFr;
    }

    public String getNameEn() {
        return nameEN;
    }

    public void setNameEn(String nameEn) {
        this.nameEN = nameEn;
    }

    public Long getIdMuscularGroup() {
        return idMuscularGroup;
    }

    public void setIdMuscularGroup(Long idMuscularGroup) {
        this.idMuscularGroup = idMuscularGroup;
    }

}
