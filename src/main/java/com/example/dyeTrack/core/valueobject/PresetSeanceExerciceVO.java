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
}