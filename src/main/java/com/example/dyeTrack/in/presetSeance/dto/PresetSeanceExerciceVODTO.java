package com.example.dyeTrack.in.presetSeance.dto;

import com.example.dyeTrack.core.entity.Equipement;
import com.example.dyeTrack.core.entity.Lateralite;
import com.example.dyeTrack.in.exercise.dto.ExerciceUltraLightReturnDTO;

public class PresetSeanceExerciceVODTO {
    private ExerciceUltraLightReturnDTO exercice;
    private String parameter;
    private Integer rangeRepInf;
    private Integer ordreExercise;
    private Integer rangeRepSup;
    private Lateralite lateralite;
    private Equipement equipement;

    public PresetSeanceExerciceVODTO(ExerciceUltraLightReturnDTO exerciceLightReturnDTO, String parameter,
            Integer rangeRepInf, Integer rangeRepSup, Integer ordreExercise,
            Lateralite lateralite, Equipement equipement) {
        this.exercice = exerciceLightReturnDTO;
        this.parameter = parameter;
        this.rangeRepInf = rangeRepInf;
        this.rangeRepSup = rangeRepSup;
        this.ordreExercise = ordreExercise;
        this.lateralite = lateralite;
        this.equipement = equipement;
    }

    public PresetSeanceExerciceVODTO() {
    }

    public ExerciceUltraLightReturnDTO getExercice() {
        return exercice;
    }

    public String getParameter() {
        return parameter;
    }

    public Integer getOrdreExercise() {
        return ordreExercise;
    }

    public Integer getRangeRepInf() {
        return rangeRepInf;
    }

    public Integer getRangeRepSup() {
        return rangeRepSup;
    }

    public Lateralite getLateralite() {
        return lateralite;
    }

    public Equipement getEquipement() {
        return equipement;
    }

    public void setExercice(ExerciceUltraLightReturnDTO exercice) {
        this.exercice = exercice;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public void setRangeRepInf(Integer rangeRepInf) {
        this.rangeRepInf = rangeRepInf;
    }

    public void setOrdreExercice(Integer ordre) {
        this.ordreExercise = ordre;
    }

    public void setRangeRepSup(Integer rangeRepSup) {
        this.rangeRepSup = rangeRepSup;
    }

    public void setLateralite(Lateralite lateralite) {
        this.lateralite = lateralite;
    }

    public void setEquipement(Equipement equipement) {
        this.equipement = equipement;
    }
}