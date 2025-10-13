package com.example.dyeTrack.in.muscle.dto;

import com.example.dyeTrack.core.entity.Muscle;

public class ReturnMuscleDTO {

    private Long idMuscle;
    private String nameFr;
    private String nameEn;
    private Long idMuscularGroup;

    public ReturnMuscleDTO() {
    }

    public ReturnMuscleDTO(Muscle muscle) {
        this.idMuscle = muscle.getId();
        this.nameEn = muscle.getNameEN();
        this.nameFr = muscle.getNameFR();
        this.idMuscularGroup = muscle.getMuscleGroup().getId();
    }

    public Long getIdMuscle() {
        return idMuscle;
    }

    public void setIdMuscle(Long idMuscle) {
        this.idMuscle = idMuscle;
    }

    public String getNameFr() {
        return nameFr;
    }

    public void setNameFr(String nameFr) {
        this.nameFr = nameFr;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public Long getIdMuscularGroup() {
        return idMuscularGroup;
    }

    public void setIdMuscularGroup(Long idMuscularGroup) {
        this.idMuscularGroup = idMuscularGroup;
    }

}
