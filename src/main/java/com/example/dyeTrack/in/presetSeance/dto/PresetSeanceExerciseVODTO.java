package com.example.dyeTrack.in.presetSeance.dto;

import com.example.dyeTrack.in.exercise.dto.ExerciseUltraLightReturnDTO;

public class PresetSeanceExerciseVODTO {
    private ExerciseUltraLightReturnDTO exercise;
    private String parameter;
    private Integer rangeRepInf;
    private Integer ordreExercise;
    private Integer rangeRepSup;
    private Long idLateralite;
    private Long idEquipment;

    public PresetSeanceExerciseVODTO(ExerciseUltraLightReturnDTO exerciseLightReturnDTO, String parameter,
            Integer rangeRepInf, Integer rangeRepSup, Integer ordreExercise,
            Long idLateralite, Long idEquipment) {
        this.exercise = exerciseLightReturnDTO;
        this.parameter = parameter;
        this.rangeRepInf = rangeRepInf;
        this.rangeRepSup = rangeRepSup;
        this.ordreExercise = ordreExercise;
        this.idLateralite = idLateralite;
        this.idEquipment = idEquipment;
    }

    public PresetSeanceExerciseVODTO() {
    }

    public ExerciseUltraLightReturnDTO getExercise() {
        return exercise;
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

    public Long getIdLateralite() {
        return idLateralite;
    }

    public Long getIdEquipment() {
        return idEquipment;
    }

    public void setExercise(ExerciseUltraLightReturnDTO exercise) {
        this.exercise = exercise;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public void setRangeRepInf(Integer rangeRepInf) {
        this.rangeRepInf = rangeRepInf;
    }

    public void setOrdreExercise(Integer ordre) {
        this.ordreExercise = ordre;
    }

    public void setRangeRepSup(Integer rangeRepSup) {
        this.rangeRepSup = rangeRepSup;
    }

    public void setIdLateralite(Long idLateralite) {
        this.idLateralite = idLateralite;
    }

    public void setIdEquipment(Long idEquipment) {
        this.idEquipment = idEquipment;
    }
}