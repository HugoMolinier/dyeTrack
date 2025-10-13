package com.example.dyeTrack.core.valueobject;

import com.example.dyeTrack.core.entity.setOfPlannedExercise.SetOfPlannedExercise.SetType;

public class SetOfPlannedExerciseVO {

    private int setOrder;
    private int repsNumber;
    private int rir;
    private double charge;
    private SetType typeOfSet;

    public SetOfPlannedExerciseVO() {
    }

    public SetOfPlannedExerciseVO(int setOrder, int repsNumber, int rir, double charge, SetType typeOfSet) {
        this.setOrder = setOrder;
        this.repsNumber = repsNumber;
        this.rir = rir;
        this.charge = charge;
        this.typeOfSet = typeOfSet;
    }

    // --- Getters & Setters ---
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