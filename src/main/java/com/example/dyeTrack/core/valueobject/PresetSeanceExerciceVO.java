package com.example.dyeTrack.core.valueobject;

public class PresetSeanceExerciceVO {
    private Long idExercice;
    private String parameter;
    private Integer rangeRepInf;
    private Integer rangeRepSup;
    private Long idLateralite;
    private Long idEquipement;

    public PresetSeanceExerciceVO(Long idExercice, String parameter, Integer rangeRepInf, Integer rangeRepSup,
            Long idLateralite, Long idEquipement) {
        this.idExercice = idExercice;
        this.parameter = parameter;
        this.rangeRepInf = rangeRepInf;
        this.rangeRepSup = rangeRepSup;
        this.idLateralite = idLateralite;
        this.idEquipement = idEquipement;
    }

    public PresetSeanceExerciceVO() {
    }

    public Long getIdExercice() {
        return idExercice;
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

    public Long getIdEquipement() {
        return idEquipement;
    }

    public void setIdExercice(Long idExercice) {
        this.idExercice = idExercice;
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

    public void setIdEquipement(Long idEquipement) {
        this.idEquipement = idEquipement;
    }
}