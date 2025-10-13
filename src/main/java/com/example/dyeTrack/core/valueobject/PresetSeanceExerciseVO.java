package com.example.dyeTrack.core.valueobject;

public class PresetSeanceExerciseVO {
    private Long idExercise;
    private String parameter;
    private Integer rangeRepInf;
    private Integer rangeRepSup;
    private Long idLateralite;
    private Long idEquipment;

    public PresetSeanceExerciseVO(Long idExercise, String parameter, Integer rangeRepInf, Integer rangeRepSup,
            Long idLateralite, Long idEquipment) {
        this.idExercise = idExercise;
        this.parameter = parameter;
        this.rangeRepInf = rangeRepInf;
        this.rangeRepSup = rangeRepSup;
        this.idLateralite = idLateralite;
        this.idEquipment = idEquipment;
    }

    public PresetSeanceExerciseVO() {
    }

    public Long getIdExercise() {
        return idExercise;
    }

    public String getParameter() {
        return parameter;
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

    public void setIdExercise(Long idExercise) {
        this.idExercise = idExercise;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public void setRangeRepInf(Integer rangeRepInf) {
        this.rangeRepInf = rangeRepInf;
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