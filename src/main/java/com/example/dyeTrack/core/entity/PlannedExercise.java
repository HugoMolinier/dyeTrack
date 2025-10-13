package com.example.dyeTrack.core.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.dyeTrack.core.entity.setOfPlannedExercise.SetOfPlannedExercise;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;

@Entity
public class PlannedExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int exerciseOrder;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "seance_track_id", nullable = false)
    @JsonIgnore
    private SeanceTrack seanceTrack;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "lateralite_id", nullable = false)
    private Lateralite lateralite;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    @OneToMany(mappedBy = "plannedExercise", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SetOfPlannedExercise> setsOfPlannedExercise = new ArrayList<>();

    // --- Getter & Setter pour la liste ---
    public List<SetOfPlannedExercise> getSetsOfPlannedExercise() {
        return setsOfPlannedExercise;
    }

    public void setSetsOfPlannedExercise(List<SetOfPlannedExercise> setsOfPlannedExercise) {
        this.setsOfPlannedExercise = setsOfPlannedExercise;
    }

    // Constructeur vide pour JPA
    public PlannedExercise() {
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getExerciseOrder() {
        return exerciseOrder;
    }

    public void setExerciseOrder(int exerciseOrder) {
        this.exerciseOrder = exerciseOrder;
    }

    public SeanceTrack getSeanceTrack() {
        return seanceTrack;
    }

    public void setSeanceTrack(SeanceTrack seanceTrack) {
        this.seanceTrack = seanceTrack;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public Lateralite getLateralite() {
        return lateralite;
    }

    public void setLateralite(Lateralite lateralite) {
        this.lateralite = lateralite;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }
}
