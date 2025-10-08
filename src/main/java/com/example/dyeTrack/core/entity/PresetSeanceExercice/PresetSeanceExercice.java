package com.example.dyeTrack.core.entity.PresetSeanceExercice;

import com.example.dyeTrack.core.entity.Equipement;
import com.example.dyeTrack.core.entity.Exercise;
import com.example.dyeTrack.core.entity.Lateralite;
import com.example.dyeTrack.core.entity.PresetSeance;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class PresetSeanceExercice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_preset", nullable = false)
    private PresetSeance presetSeance;

    @ManyToOne
    @JoinColumn(name = "exercice_id")
    private Exercise exercice;

    private String parameter;
    private Integer rangeRepInf;
    private Integer rangeRepSup;

    @Column(name = "order_exercice")
    private Integer orderExercice;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "lateralite_id", nullable = false)
    private Lateralite lateralite;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "equipement_id", nullable = false)
    private Equipement equipement;

    // --- Constructeurs ---
    public PresetSeanceExercice() {
    }

    public PresetSeanceExercice(PresetSeance presetSeance, Exercise exercice,
            String parameter, Integer rangeRepInf, Integer rangeRepSup,
            Integer orderExercice, Lateralite lateralite, Equipement equipement) {
        this.presetSeance = presetSeance;
        this.exercice = exercice;
        this.parameter = parameter;
        this.rangeRepInf = rangeRepInf;
        this.rangeRepSup = rangeRepSup;
        this.orderExercice = orderExercice;
        this.lateralite = lateralite;
        this.equipement = equipement;
    }

    // --- Getters & Setters ---
    public Long getId() {
        return id;
    }

    public PresetSeance getPresetSeance() {
        return presetSeance;
    }

    public Exercise getExercice() {
        return exercice;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public Integer getRangeRepInf() {
        return rangeRepInf;
    }

    public void setRangeRepInf(Integer rangeRepInf) {
        this.rangeRepInf = rangeRepInf;
    }

    public Integer getRangeRepSup() {
        return rangeRepSup;
    }

    public void setRangeRepSup(Integer rangeRepSup) {
        this.rangeRepSup = rangeRepSup;
    }

    public Integer getOrderExercice() {
        return orderExercice;
    }

    public void setOrderExercice(Integer orderExercice) {
        this.orderExercice = orderExercice;
    }

    public Lateralite getLateralite() {
        return lateralite;
    }

    public void setLateralite(Lateralite lateralite) {
        this.lateralite = lateralite;
    }

    public Equipement getEquipement() {
        return equipement;
    }

    public void setEquipement(Equipement equipement) {
        this.equipement = equipement;
    }
}
