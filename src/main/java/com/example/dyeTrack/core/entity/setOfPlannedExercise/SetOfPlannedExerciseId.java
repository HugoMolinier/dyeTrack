package com.example.dyeTrack.core.entity.setOfPlannedExercise;

import java.io.Serializable;
import java.util.Objects;

public class SetOfPlannedExerciseId implements Serializable {

    private Long plannedExercise;
    private int setOrder;

    public SetOfPlannedExerciseId() {
    }

    public SetOfPlannedExerciseId(Long plannedExercise, int setOrder) {
        this.plannedExercise = plannedExercise;
        this.setOrder = setOrder;
    }

    // --- equals & hashCode ---
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof SetOfPlannedExerciseId))
            return false;
        SetOfPlannedExerciseId that = (SetOfPlannedExerciseId) o;
        return setOrder == that.setOrder &&
                Objects.equals(plannedExercise, that.plannedExercise);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plannedExercise, setOrder);
    }

    // --- Getters & Setters ---
    public Long getPlannedExercise() {
        return plannedExercise;
    }

    public void setPlannedExercise(Long plannedExercise) {
        this.plannedExercise = plannedExercise;
    }

    public int getSetOrder() {
        return setOrder;
    }

    public void setSetOrder(int setOrder) {
        this.setOrder = setOrder;
    }
}
