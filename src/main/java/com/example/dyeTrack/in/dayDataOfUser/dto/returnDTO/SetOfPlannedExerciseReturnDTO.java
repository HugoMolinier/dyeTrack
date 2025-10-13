package com.example.dyeTrack.in.dayDataOfUser.dto.returnDTO;

import jakarta.persistence.*;

import com.example.dyeTrack.core.entity.setOfPlannedExercise.SetOfPlannedExercise;
import com.example.dyeTrack.core.entity.setOfPlannedExercise.SetOfPlannedExercise.SetType;

public class SetOfPlannedExerciseReturnDTO {

    private int setOrder;

    private int repsNumber;
    private int rir;
    private double charge;

    @Enumerated(EnumType.STRING)
    private SetType typeOfSet;

    public SetOfPlannedExerciseReturnDTO() {
    }

    public SetOfPlannedExerciseReturnDTO(SetOfPlannedExercise setOfPlannedExercise) {
        this.setOrder = setOfPlannedExercise.getSetOrder();
        this.repsNumber = setOfPlannedExercise.getRepsNumber();
        this.rir = setOfPlannedExercise.getRir();
        this.charge = setOfPlannedExercise.getCharge();
        this.typeOfSet = setOfPlannedExercise.getTypeOfSet();

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
