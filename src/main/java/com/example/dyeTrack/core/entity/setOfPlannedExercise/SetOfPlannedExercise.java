package com.example.dyeTrack.core.entity.setOfPlannedExercise;

import jakarta.persistence.*;

import com.example.dyeTrack.core.entity.PlannedExercise;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@IdClass(SetOfPlannedExerciseId.class)
public class SetOfPlannedExercise {

    public enum SetType {
        WARMUP, EFFECTIVE, TEMPO
    }

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "planned_exercise_id", nullable = false)
    @JsonIgnore
    private PlannedExercise plannedExercise;

    @Id
    private int setOrder;

    private int repsNumber;
    private int rir;
    private double charge;

    @Enumerated(EnumType.STRING)
    private SetType typeOfSet;

    public SetOfPlannedExercise() {
    }

    // --- Getters & Setters ---
    public PlannedExercise getPlannedExercise() {
        return plannedExercise;
    }

    public void setPlannedExercise(PlannedExercise plannedExercise) {
        this.plannedExercise = plannedExercise;
    }

    public int getSetOrder() {
        return setOrder;
    }

    public void setSetOrder(int setOrder) {
        this.setOrder = setOrder;
    }

    public int getRepsNumber() {
        return repsNumber;
    }

    public void setRepsNumber(int repsNumber) {
        this.repsNumber = repsNumber;
    }

    public int getRir() {
        return rir;
    }

    public void setRir(int rir) {
        this.rir = rir;
    }

    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }

    public SetType getTypeOfSet() {
        return typeOfSet;
    }

    public void setTypeOfSet(SetType typeOfSet) {
        this.typeOfSet = typeOfSet;
    }
}
